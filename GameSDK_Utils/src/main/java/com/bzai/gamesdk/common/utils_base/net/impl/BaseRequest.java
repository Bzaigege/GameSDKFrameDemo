package com.bzai.gamesdk.common.utils_base.net.impl;

import android.text.TextUtils;

import com.bzai.gamesdk.common.utils_base.cache.ApplicationCache;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.AuthFailureError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.NetworkResponse;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.Request;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.Response;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.VolleyError;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.toolbox.HttpHeaderParser;
import com.bzai.gamesdk.common.utils_base.net.base.VolleyRequestWrapper;
import com.bzai.gamesdk.common.utils_base.net.base.VolleyResponseListener;
import com.bzai.gamesdk.common.utils_base.utils.CodeUtils;
import com.bzai.gamesdk.common.utils_base.utils.JsonUtils;
import com.bzai.gamesdk.common.utils_base.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by bzai on 2018/04/09.
 *
 * Volley网络请求基类，所有的网络请求都继承这个类
 */
public class BaseRequest extends VolleyRequestWrapper {

    private static final String TAG = "BaseRequest";

    private int method;
    private String url;
    private Map<String, Object> mParamMap = new HashMap<>();

    private String authorization;
    private String userAgent;
    private Map<String, String> headers;

    private BaseRequest(int method, String url, VolleyResponseListener listener) {
        super(ApplicationCache.getInstance().getApplicationContext(), method, url, listener);
    }


    /*GET 和 POST 需要额外处理下url 和 bodyMap**/
    public BaseRequest(int method, String url, Map<String, Object> bodyMap, VolleyResponseListener listener) {
        this(method, method == Method.GET ? buildParamsUrl(url, bodyMap) : url,listener);
        this.method = method;
        this.url = url;
        this.mParamMap = bodyMap == null ? mParamMap : bodyMap;
    }


    /**
     * 拼接Get请求链接
     * @param url
     * @param args Get请求的参数
     * @return
     */
    private static String buildParamsUrl(String url, Map<String,Object> args){

        url += "?";
        if (args == null) {
            args = new HashMap<>();
        }
        return  CodeUtils.appendParams(new StringBuilder(url),args);
    }

    /**
     * 封装请求头
     * @return
     */
    @Override
    public Map<String, String> getHeaders() {

        String header = getAuthorization(method == Request.Method.GET ? "GET" : "POST", url, mParamMap);
        String userAgentValue = getUserAgent();
        headers = new HashMap<>();
        headers.put("Authorization", header);
        headers.put("User-Agent", userAgentValue);
        // 默认启动gzip压缩
        headers.put("Accept-Encoding", "gzip");
        return headers;
    }

    /**
     * 对外提供接口来设置 authorization
     * @param authorization
     */
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization(String method, String url, Map<String, ?> requestParams) {
        if (!TextUtils.isEmpty(authorization)) {
            return authorization;
        }

        return BaseRequestUtils.getOAuthHeader(method,url,requestParams);
    }

    /**
     * 对外提供接口来设置 userAgent
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    private String getUserAgent() {
        if (!TextUtils.isEmpty(userAgent)) {
            return userAgent;
        }
        return BaseRequestUtils.getUserAgent();
    }

    /**
     * 封装请求body内容
     * @return
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] content = null;
        String mStrBody = CodeUtils.appendParams(new StringBuilder(),mParamMap);
        try {
            content = mStrBody.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 根据网络返回数据，进行解压解密等操作
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        if (response == null || response.data == null) {
            return Response.error(new VolleyError());
        }

        byte[] data;// volley缓存是针对处理后的response对象，故只针对response的data进行处理
        if (response.headers.containsKey("Content-Encoding") && response.headers.get("Content-Encoding").equals("gzip")) {
            data = decompressZipToByte(response.data);
            if (data == null) {
                return Response.error(new VolleyError());
            }
        } else {
            data = response.data;
        }
        Response<String> rsp = parseResponse(response, data);
        if (rsp != null) {
            return rsp;
        } else {
            return Response.error(new VolleyError());
        }
    }


    /**
     * 解压byte数组为对应的字符串
     *
     * @param body
     * @return
     */
    public static byte[] decompressZipToByte(byte[] body) {

        if (body == null || body.length == 0) {
            return null;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(body);
            // 判断是否是GZIP格式
            int ss = (body[0] & 0xff) | ((body[1] & 0xff) << 8);
            if (ss == GZIPInputStream.GZIP_MAGIC) {
                GZIPInputStream gunzip = new GZIPInputStream(in);
                byte[] buffer = new byte[256];
                int n;
                while ((n = gunzip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                return out.toByteArray();
            } else {
                // 非gzip压缩，直接返回结果
                return body;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 解析请求成功返回数据
     *
     * @param response
     * @param data
     * @return
     */
    protected Response<String> parseResponse(NetworkResponse response, byte[] data) {
        String parsed = null;
        try {
            parsed = new String(data, "UTF-8");
            printNetInfo(true, response, parsed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


    /**
     * 解析请求失败返回数据
     *
     * @param volleyError the error retrieved from the network
     * @return
     */
    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (volleyError.networkResponse != null) {
            printNetInfo(false, volleyError.networkResponse, new String(volleyError.networkResponse.data));
        } else {
            printNetInfo(false, null, volleyError.toString());
        }
        return super.parseNetworkError(volleyError);
    }

    /**
     * 输出网络请求详情日志
     *
     * @param response
     * @param parsed
     */
    private void printNetInfo(boolean success, NetworkResponse response, String parsed) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("network request info");
            sb.append("\nmethod:" + (this.getMethod() == Method.GET ? "get" : "post"));
            sb.append("\nurl:" + URLDecoder.decode(this.getUrl(), "UTF-8"));
            sb.append("\nheaders:" + this.headers);
            sb.append("\nAuthorization:" + this.headers.get("Authorization"));
            sb.append("\nbody:" + URLDecoder.decode(new String(this.getBody()), "UTF-8"));
            sb.append("\nstatusCode:" + (response != null ? response.statusCode : "-1"));
            sb.append("\nnetworkTimeMs:" + (response != null ? response.networkTimeMs : "15000"));
            if (!TextUtils.isEmpty(parsed))
                sb.append("\nresponse:\n" + CodeUtils.Unicode2GBK(JsonUtils.isJson(parsed) ? JsonUtils.format(parsed) : parsed));

            if (!success)
                LogUtils.debug_i(TAG, sb.toString());
            else
                LogUtils.debug_i(TAG, sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 发送网络请求
     */
    @Override
    public void sendRequest() {
        super.sendRequest();
        try {
            LogUtils.debug_i(TAG, "url:" + URLDecoder.decode(this.getUrl(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消网络请求
     */
    @Override
    public void cancel() {
        super.cancel();
        try {
            LogUtils.debug_i(TAG, "cancel url:" + URLDecoder.decode(this.getUrl(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
