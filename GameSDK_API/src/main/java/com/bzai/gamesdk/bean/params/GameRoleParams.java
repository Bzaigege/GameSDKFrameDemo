package com.bzai.gamesdk.bean.params;

/**
 * Created by bzai on 2018/4/18.
 * <p>
 * Desc:
 *
 *  数据上报透传实体类,只提供set方法，不对外提供get方法
 *
 *  ReportId            数据上传类型：
 *
 *      1:进入游戏
 *      2:登录游戏
 *      3:选择区服
 *      4:角色创建
 *      5:角色升级
 *      6:角色支付
 *      7:退出游戏
 *
 *  roleId			    角色ID
 *  roleName			角色名称
 *  roleLevel			角色等级
 *  roleCreateTime      角色创建时间 （选填）
 *  roleVipLevel        角色vip等级 （选填）
 *  roleUpLevelTime     角色升级时间 （选填）
 *  roleUnionName       角色所在家族/工会名称 (选填)
 *  roleBalance         角色充值余额(选填)
 *
 *  serverId		    登录所属的游戏服务器唯一标识
 *  serverName			登录所属的游戏服务器名称
 *
 *  extraInfo           拓展字段信息
 *
 */

public class GameRoleParams {

    //时间类型，可扩展
    private int ReportId;

    //角色信息
    private String roleId;
    private String roleName;
    private String roleLevel;
    private long roleCreateTime;
    private String roleVipLevel;
    private long roleUpLevelTime;
    private String roleUnionName;
    private float roleBalance ;

    //区服信息
    private String serverId;
    private String serverName;

    private String extraInfo;

    public void setReportId(int reportId) {
        ReportId = reportId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setRoleCreateTime(long roleCreateTime) {
        this.roleCreateTime = roleCreateTime;
    }

    public void setRoleVipLevel(String roleVipLevel) {
        this.roleVipLevel = roleVipLevel;
    }

    public void setRoleUpLevelTime(long roleUpLevelTime) {
        this.roleUpLevelTime = roleUpLevelTime;
    }

    public void setRoleUnionName(String roleUnionName) {
        this.roleUnionName = roleUnionName;
    }

    public void setRoleBalance(float roleBalance) {
        this.roleBalance = roleBalance;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
