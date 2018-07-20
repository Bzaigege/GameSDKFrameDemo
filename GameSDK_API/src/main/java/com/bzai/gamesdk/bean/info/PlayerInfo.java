package com.bzai.gamesdk.bean.info;

/**
 * Created by bzai on 2018/4/11.
 * <p>
 * Desc:  返回外界的用户信息实体类
 */

public class PlayerInfo {

    private String playerId;
    private String token;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
