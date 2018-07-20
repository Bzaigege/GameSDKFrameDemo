package com.bzai.gamesdk.common.utils_base.net;

import com.bzai.gamesdk.common.utils_base.net.request.IRequestManager;
import com.bzai.gamesdk.common.utils_base.net.request.RequestCallback;
import com.bzai.gamesdk.common.utils_base.net.request.VolleyRequestManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bzai on 2018/04/09.
 * 网络请求接口类
 *
 * 网络框架要替换成别的时候，实现具体封装就OK了,并修改具体实现
 * 比如换成okhttp写法 ：return new OkHttpRequestManager();
 *
 */

public class RequestExecutor {

    public static final String GET = "GET";
    public static final String POST = "POST";

    private IRequestManager iRequestManager;

    private String method;
    private String url;
    private String header;
    private String userAgent;
    private Map<String,Object> params;
    private RequestCallback requestCallback;

    private RequestExecutor(RequestExecutor.Builder builder){

        this.method = builder.method;
        this.url = builder.url;
        this.header = builder.header;
        this.userAgent = builder.userAgent;
        this.params = builder.params;
        this.requestCallback = builder.requestCallback;
    }

    public void startRequest(){

        iRequestManager = new VolleyRequestManager();
        iRequestManager.setHeader(header);
        iRequestManager.setUserAgent(userAgent);

        if (RequestExecutor.GET.equals(method)){

            iRequestManager.get(url,params,requestCallback);

        }else if (RequestExecutor.POST.equals(method)){

            iRequestManager.post(url,params,requestCallback);
        }

    }

    /**
     * 取消当前的网络请求，
     */
    public void cancel(){
        iRequestManager.cancel();
    }

    public static class Builder{

        private String method;
        private String url;
        private String header;
        private String userAgent;
        private Map<String,Object> params;
        private RequestCallback requestCallback;

        public RequestExecutor build(){
            return new RequestExecutor(this);
        }

        public RequestExecutor.Builder setMethod(String method){
            this.method = method;
            return this;
        }

        public RequestExecutor.Builder setUrl(String url){
            this.url = url;
            return this;
        }

        public RequestExecutor.Builder setHeader(String header){
            this.header = header;
            return this;
        }

        public RequestExecutor.Builder setUserAgent(String userAgent){
            this.userAgent = userAgent;
            return this;
        }

        public RequestExecutor.Builder setParams(HashMap<String,Object> params){
            this.params = params;
            return this;
        }

        public RequestExecutor.Builder setRequestCallback(RequestCallback requestCallback){
            this.requestCallback = requestCallback;
            return this;
        }

    }

}
