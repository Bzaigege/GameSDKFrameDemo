package com.bzai.gamesdk.bean.params;

/**
 * Created by bzai on 2018/4/13.
 * <p>
 * Desc:
 *
 *  支付接口的透传实体类,只提供set方法，不对外提供get方法
 *
 *  注意：字段需与服务端一致，防止后续自动转化时出问题
 *
 *  productID			商品ID
 *  productName			商品名称
 *  productDesc			商品描述
 *  productNumber       商品数量 (暂时没有)
 *
 *  money				商品单价 (以分为单位)
 *  coinName			货币名称(人民币/台币/美元..) (暂时没有)
 *	coinRate			货币对人民币的比例 (暂时没有)
 *
 *  roleID              当前游戏内角色ID
 *  roleName            当前游戏内角色名称
 *  roleLevel           玩家等级
 *  serverID            当前玩家所在的服务器ID
 *  serverName          当前玩家所在的服务器名称
 *
 *  orderNo				游戏的订单号 (暂时没有)
 *  rate                游戏币与人民币比例  (暂时没有)
 *  callback            支付回调地址
 *	extension			透传给 cp服务器的字段
 *
 *  gorder              CP订单号
 */

public class PayParams {

    //商品信息
    private String productID;
    private String productName;
    private String productDesc;

    //金额信息
    private int money;

    //玩家信息
    private String roleID;
    private String roleName;
    private String roleLevel;
    private String serverID;
    private String serverName;

    //支付配置信息
    private String callback;
    private String extension;

    //游戏订单号
    private String gorder;

    public void setProductId(String productId) {
        this.productID = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setNotifyUrl(String callback) {
        this.callback = callback;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setGorder(String gorder) {
        this.gorder = gorder;
    }
}
