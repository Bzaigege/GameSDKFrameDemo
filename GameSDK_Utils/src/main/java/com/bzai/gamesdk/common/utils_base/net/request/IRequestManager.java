package com.bzai.gamesdk.common.utils_base.net.request;

import java.util.Map;

/**
 * Created by bzai on 2018/04/09.
 *
 * 将网络请求方法抽象抽离出来
 */

public interface IRequestManager {

    void get(String url, Map<String, Object> params, RequestCallback requestCallback);

    void post(String url, Map<String, Object> params, RequestCallback requestCallback);

    void cancel();

    void setHeader(String header);

    void setUserAgent(String UserAgent);

}
