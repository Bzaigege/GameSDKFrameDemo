package com.bzai.gamesdk.module.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bzai.gamesdk.common.utils_base.config.ErrCode;
import com.bzai.gamesdk.common.utils_base.config.TypeConfig;
import com.bzai.gamesdk.common.utils_base.interfaces.CallBackListener;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;
import com.bzai.gamesdk.common.utils_business.cache.BaseCache;
import com.bzai.gamesdk.common.utils_business.config.KeyConfig;
import com.bzai.gamesdk.module.account.bean.AccountBean;
import com.bzai.gamesdk.module.account.bean.AccountCallBackBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *
 *  账号管理类，管理SDK的各个功能接口：登录、切换账号、注销账号、绑定账号。
 *
 *  注意可能还会有各个复杂的登录、绑定等逻辑。
 *
 *  后续项目待定
 *
 */

public class AccountManager {

    public static final String TAG = "AccountManager";

    private volatile static AccountManager INSTANCE;

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AccountManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AccountManager();
                }
            }
        }
        return INSTANCE;
    }

    private Activity mActivity;
    private AccountBean mLoginInfo; //当前登陆的登陆信息
    private boolean isSwitchAccount = false; //通过标记位来判断是否是切换账号按钮的登录回调


    /******************************************      获取Project账号监听     ****************************************/

    private CallBackListener projectLoginCallBackListener;
    public void setLoginCallBackLister(CallBackListener callBackLister){
        projectLoginCallBackListener = callBackLister;
    }

    private void CallBackToProject(int event, int code, AccountBean accountBean, String msg){

        //设置回调信息
        AccountCallBackBean accountCallBackBean = new AccountCallBackBean();
        accountCallBackBean.setEvent(event); //事件类型ID
        accountCallBackBean.setErrorCode(code); //事件码
        accountCallBackBean.setAccountBean(accountBean); //事件的账号信息
        accountCallBackBean.setMsg(msg); //设置事件的信息

        if (projectLoginCallBackListener != null){
            projectLoginCallBackListener.onSuccess(accountCallBackBean);//回调给Project的信息
        }
    }


    /**
     * 登录结果监听
     */
    private CallBackListener LoginCallBackLister = new CallBackListener<AccountBean>(){

        @Override
        public void onSuccess(AccountBean loginInfo) {
            LogUtils.d(TAG, "loginInfo：" + loginInfo.toString());

            mLoginInfo = loginInfo;
            //登陆成功，设置登录信息
            setLoginSuccess(loginInfo);

            if (isSwitchAccount){
                CallBackToProject(TypeConfig.SWITCHACCOUNT, ErrCode.SUCCESS,loginInfo, "user switchAccount success");
                isSwitchAccount = false; //置为false

            }else {
                CallBackToProject(TypeConfig.LOGIN,ErrCode.SUCCESS,loginInfo, "user login success");
            }
        }

        @Override
        public void onFailure(int code, String msg) {
            mLoginInfo = null; //当前登陆失败就置为null

            if (isSwitchAccount){

                if (code == ErrCode.CANCEL){ //如果切换账号时,不走登录,给登出回调
                    CallBackToProject(TypeConfig.LOGOUT, ErrCode.SUCCESS, null, "user logout success");

                }else {
                    CallBackToProject(TypeConfig.SWITCHACCOUNT, code, null, msg);
                }

            }else {
                CallBackToProject(TypeConfig.LOGIN, code, null, msg);
            }
        }
    };

    /******************************************      登录     ****************************************/


    /**
     * 显示登录界面
     */
    public void showLoginView(final Activity activity, HashMap<String,Object> loginMap){
        mActivity = activity;


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("是否登录?");
        builder.setTitle("登录界面");
        builder.setPositiveButton("登录",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {

                        AccountBean loginInfo = new AccountBean();
                        loginInfo.setLoginState(true); //将登录成功状态返回
                        loginInfo.setUserToken("dasfkaf-SAFA-kfad");
                        loginInfo.setUserID("userID-123");
                        loginInfo.setUserName("测试用户"); //聚合将用名设置为UserID
                        LoginCallBackLister.onSuccess(loginInfo);

                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        LoginCallBackLister.onFailure(ErrCode.FAILURE,"login fail");
                    }
                });
        builder.create().show();

    }


    /**
     * 授权登录,具体项目具体实现逻辑
     */
    public void authLogin(Activity activity, HashMap<String,Object> loginMap){
        mActivity = activity;

        AccountBean loginInfo = new AccountBean();
        loginInfo.setLoginState(true); //将登录成功状态返回
        loginInfo.setUserToken("dasfkaf-SAFA-kfad");
        loginInfo.setUserID("userID-123");
        loginInfo.setUserName("测试用户"); //聚合将用名设置为UserID

        LoginCallBackLister.onSuccess(loginInfo);
    }


    /**
     * 获取当前登陆状态,默认false
     * @return
     */
    public boolean getLoginState() {
        if (mLoginInfo != null){
            return mLoginInfo.getLoginState();
        }
        return false;
    }

    /******************************************      切换账号    ****************************************/

    /**
     * 切换账号
     * @param activity
     */
    public void switchAccount(Activity activity){

        mActivity = activity;

        //先走登出逻辑
        mLoginInfo = null; //登录信息清空
        isSwitchAccount = true;
        clearLoginInfo(activity);
    }


    /******************************************      登出   ****************************************/

    /**
     * 账号登出
     */
    public void logout(Activity activity){

        mActivity = activity;

        mLoginInfo = null; //登录信息清空
        isSwitchAccount = false;
        clearLoginInfo(activity);

        CallBackToProject(TypeConfig.LOGOUT, ErrCode.SUCCESS, null, "user logout success");
    }


    /**
     * 设置登录成功行为
     */
    private void setLoginSuccess(AccountBean loginInfo){

        if (loginInfo != null){
            BaseCache.getInstance().put(KeyConfig.PLAYER_ID,loginInfo.getUserID());
            BaseCache.getInstance().put(KeyConfig.PLAYER_NAME,loginInfo.getUserName());
            BaseCache.getInstance().put(KeyConfig.PLAYER_TOKEN,loginInfo.getUserToken());

            //将当前状态存储到全局变量供其他模块插件使用
            BaseCache.getInstance().put(KeyConfig.IS_LOGIN, getLoginState());
        }
    }


    /**
     * 清空登陆信息
     */
    private void clearLoginInfo(Activity activity){

        mLoginInfo = null;

        //清空内存的用户信息
        BaseCache.getInstance().put(KeyConfig.PLAYER_ID,"");
        BaseCache.getInstance().put(KeyConfig.PLAYER_NAME,"");
        BaseCache.getInstance().put(KeyConfig.PLAYER_TOKEN,"");

        //将当前状态存储到全局变量供其他模块插件使用
        BaseCache.getInstance().put(KeyConfig.IS_LOGIN, getLoginState());

    }

}
