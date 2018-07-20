package com.bzai.gamesdk;

/**
 * Created by bzai on 2018/4/10.
 * <p>
 * Desc:
 *
 *    用于对外给CP获取对应的游戏 gameid 和 gamekey
 */

public class GameInfoSetting {

    public String gameid;
    public String gamekey;

    public GameInfoSetting(String gameid, String gamekey){

        this.gameid = gameid;
        this.gamekey = gamekey;
    }

}
