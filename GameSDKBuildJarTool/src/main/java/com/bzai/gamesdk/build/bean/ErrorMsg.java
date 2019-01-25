package com.bzai.gamesdk.build.bean;

/**
 * 描述打包结果
 */
public class ErrorMsg {

    public int code;
    public String message;
    public Exception e;

    public ErrorMsg(int code, String message){
        this.code = code;
        this.message = message;
    }

    public ErrorMsg(int code, String message, Exception e){
        this.code = code;
        this.message = message;
        this.e = e;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
