package com.bzai.gamesdkframe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bzai.gamesdk.GameInfoSetting;
import com.bzai.gamesdk.api.SDKAPI;
import com.bzai.gamesdk.bean.params.PayParams;
import com.bzai.gamesdk.listener.AccountCallBackLister;
import com.bzai.gamesdk.listener.ExitCallBackLister;
import com.bzai.gamesdk.listener.InitCallBackLister;
import com.bzai.gamesdk.listener.PurchaseCallBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class GameSDKMain extends Activity implements View.OnClickListener{

    private String TAG = getClass().getName();
    private EditText etPay,etReport;
    private TextView tvLog;
    private int pri = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
    }


    private void initView(){

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_switchAccount).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);

        etPay = (EditText) findViewById(R.id.et_inputPrice);
        tvLog = (TextView) findViewById(R.id.tvLog);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_switchAccount:
                switchAccount();
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_pay:
                purchase();
                break;

            case R.id.btn_exit:
                exitGame();
                break;
            default:
                break;
        }
    }


    private AccountCallBackLister accountCallBackLister = new AccountCallBackLister() {

        @Override
        public void onAccountEventCallBack(String jsonStr) {
            tvLog.setText(jsonStr);
            showToast(jsonStr);

            //CP解析数据格式
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                int code = jsonObject.getInt("eventType");

                if (code == AccountCallBackLister.LOGIN_SUCCESS ||
                        code == AccountCallBackLister.SWITCH_ACCOUNT_SUCCESS){

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void init() {

        //用于配置游戏的 gameid  和 gamekey
        String gameid = "1";
        String gamekey = "222";

        GameInfoSetting gameInfoSetting = new GameInfoSetting(gameid,gamekey);
        SDKAPI.getInstance().init(this, gameInfoSetting,accountCallBackLister, new InitCallBackLister() {

            @Override
            public void onSuccess() {
                tvLog.setText("初始化状态：初始化成功");
                showToast("初始化状态：初始化成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                String message = "初始化状态"
                        +"\n错误码:\n" + code
                        +"\n错误信息:\n" + msg;
                tvLog.setText(message);
                showToast(message);
            }
        });
    }


    /**
     * 账号登陆
     */
    private void login() {
        SDKAPI.getInstance().login(this);
    }

    /**
     * 切换账号
     */
    private void switchAccount(){
        SDKAPI.getInstance().switchAccount(this);
    }

    /**
     * 账号登出
     */
    private void logout() {
        SDKAPI.getInstance().logout(this);
    }

    /**
     * 购买
     */
    private void purchase() {

        String pri_str = etPay.getText().toString();
        if (!isDecimal(pri_str) && !isInteger(pri_str)) {
            showToast("Please enter Correct values.");
            return;
        }

        try {
            pri = Integer.valueOf(pri_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         *  productId			商品ID
         *  productName			商品名称
         *  productDesc			商品描述
         *
         *  money				商品单价 (以分为单位)
         *
         *  roleID              当前游戏内角色ID
         *  roleName            当前游戏内角色名称
         *  roleLevel           玩家等级
         *  serverID            当前玩家所在的服务器ID
         *  serverName          当前玩家所在的服务器名称
         *
         *  notifyUrl           支付回调地址
         *  extraInfo			透传给 cp服务器的字段
         *  gorder              CP订单号
         */

        //商品信息
        PayParams payParams = new PayParams();
        payParams.setProductName("金币");
        payParams.setProductDesc("一金币等于十银币");
        payParams.setProductId("9000"); //SDK的商品ID

        //金额信息
        payParams.setMoney(pri);

        //玩家信息
        payParams.setRoleID("123456");
        payParams.setRoleName("小明");
        payParams.setRoleLevel("15");
        payParams.setServerID("11");
        payParams.setServerName("服务十一区");

        //支付配置信息
        payParams.setNotifyUrl("www.baidu.com");
        payParams.setExtension("extra");
        payParams.setGorder("DD123456");

        SDKAPI.getInstance().pay(this, payParams,new PurchaseCallBackListener() {

            //创建SDK订单成功就返回，方便CP查询该笔订单状态
            @Override
            public void onOrderId(String orderId) {
                showToast(orderId);
            }

            @Override
            public void onSuccess() {
                String message = "支付成功";
                tvLog.setText(message);
                showToast(message);
            }

            @Override
            public void onFailure(int code, String msg) {

                String message = "支付失败\n:\n错误:\n" + code;
                tvLog.setText(message);
                showToast(message);

            }

            @Override
            public void onCancel() {
                String message = "支付取消\n:\n错误:\n" ;
                tvLog.setText(message);
                showToast(message);
            }

            //支付完成，没有正确支付回调时，会返回该结果
            @Override
            public void onComplete() {
                String message = "支付完成\n" ;
                tvLog.setText(message);
                showToast(message);
            }

        });
    }



    //--------------------------------------------------退出接口---------------------------------------------

    private void exitGame(){

        SDKAPI.getInstance().exit(GameSDKMain.this,new ExitCallBackLister(){

            //存在退出框,并且点击退出成功
            @Override
            public void onExitDialogSuccess() {
                Log.d(TAG,"退出成功");
                SDKAPI.getInstance().onDestroy(GameSDKMain.this);
                System.exit(0);
            }

            //存在退出框,并且点击取消退出
            @Override
            public void onExitDialogCancel() {
                Log.d(TAG,"退出取消");
            }

            //不存在退出框，需要游戏自己实现退出框,然后调用SDK释放资源接口
            @Override
            public void onNotExitDialog() {

                AlertDialog.Builder builder = new AlertDialog.Builder(GameSDKMain.this);
                builder.setTitle("退出游戏");
                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SDKAPI.getInstance().onDestroy(GameSDKMain.this);
                        System.exit(0);
                    }
                });
                builder.show();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitGame();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitGame();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // 浮点型判断 (Floating-point judgment)
    public static boolean isDecimal(String str) {
        if (str == null || "".equals(str))
            return false;
        java.util.regex.Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(str).matches();
    }

    // 整型判断 (Integer to determine)
    public static boolean isInteger(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }

    //--------------------------------------------------生命周期接口---------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        SDKAPI.getInstance().onStart(GameSDKMain.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SDKAPI.getInstance().onResume(GameSDKMain.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SDKAPI.getInstance().onPause(GameSDKMain.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SDKAPI.getInstance().onStop(GameSDKMain.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SDKAPI.getInstance().onRestart(GameSDKMain.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SDKAPI.getInstance().onDestroy(GameSDKMain.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SDKAPI.getInstance().onNewIntent(GameSDKMain.this,intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SDKAPI.getInstance().onActivityResult(GameSDKMain.this,requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SDKAPI.getInstance().onRequestPermissionsResult(GameSDKMain.this,requestCode, permissions, grantResults);
    }
}
