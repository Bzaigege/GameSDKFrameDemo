package com.bzai.gamesdk.common.utils_base.net.impl.bean;


import com.bzai.gamesdk.common.utils_base.proguard.ProguardObject;

/**
 * Created by bzai.xiao on 2017/12/20.
 *
 * 服务端数据格式实体
 */
public class ResponseResult<T> extends ProguardObject {

    private String ret;
    private String msg;
    private T data;

    public String getCode() {
        return ret;
    }

    public void setCode(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return data;
    }

    public void setResult(T data) {
        this.data = data;
    }

}
