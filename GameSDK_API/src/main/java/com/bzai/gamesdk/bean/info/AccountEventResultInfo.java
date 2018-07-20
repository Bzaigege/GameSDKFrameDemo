package com.bzai.gamesdk.bean.info;

/**
 * Created by bzai on 2018/4/17.
 * <p>
 * Desc:
 *
 *  用于装换成Json对象的实体类,不对外提供get方法
 *
 */

public class AccountEventResultInfo {


    private int eventType; //事件类型

    private int statusCode; //事件ID的状态码

    private String message; //描述信息

    private PlayerInfo playerInfo; //用户信息

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

}
