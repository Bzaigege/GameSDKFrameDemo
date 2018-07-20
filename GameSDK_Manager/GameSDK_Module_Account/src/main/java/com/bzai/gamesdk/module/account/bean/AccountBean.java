package com.bzai.gamesdk.module.account.bean;


/**
 * Created by bzai on 2018/4/11.
 * <p>
 * Desc:
 *
 *  登录信息，封装服务端返回信息，用于对接Project的公开实体
 *  (后续注意不同项目的字段)
 *
 */

public class AccountBean{

    private boolean loginState; //当前登陆状态
    private String userID;
    private String userName;
    private String userToken;

    public boolean getLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        return "AccountBean {" + "userID=" + userID
                + ", userName=" + userName
                + ", userToken=" + userToken + '}';
    }
}
