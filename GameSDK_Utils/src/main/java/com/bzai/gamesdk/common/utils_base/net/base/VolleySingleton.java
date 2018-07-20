package com.bzai.gamesdk.common.utils_base.net.base;

import android.content.Context;

import com.bzai.gamesdk.common.utils_base.frame.google.volley.DefaultRetryPolicy;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.Request;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.RequestQueue;
import com.bzai.gamesdk.common.utils_base.frame.google.volley.toolbox.Volley;

/**
 * Created by bzai on 2018/04/09.
 *
 * 封装Volley实例对象为单例
 */
public class VolleySingleton {

	private static volatile VolleySingleton mInstance;
	private Context mContext;
	private RequestQueue mRequestQueue;

	private VolleySingleton(Context context) {
		this.mContext = context;
		this.mRequestQueue = getRequestQueue();
	}

	public static VolleySingleton getInstance(Context context) {
		if (mInstance == null) {
			synchronized (VolleySingleton.class) {
				if (mInstance == null)
					mInstance = new VolleySingleton(context);
			}
		}
		return mInstance;
	}

	/**
	 * 获取 volley RequestQueue 实例
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(mContext);
		}
		return mRequestQueue;
	}

	/**
	 * 将Request加入RequestQueue
	 */
	public <T> void addToRequestQueue(Request<T> request) {
		if (request == null)
			return;
		request.setShouldCache(false); //不设置缓存
		request.setRetryPolicy(new DefaultRetryPolicy(15 * 1000,0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		getRequestQueue().add(request);
	}

	/**
	 * 将特定的Request从RequestQueue清除
	 * @param key
	 */
	public void removeRequest(String key) {
		getRequestQueue().getCache().remove(key);
	}

	/**
	 * 停止网络请求(所有的)
	 */
	public void destroy(){
		getRequestQueue().stop();
		mInstance = null;
	}

}
