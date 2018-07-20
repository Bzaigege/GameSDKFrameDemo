package com.bzai.gamesdk.common.utils_ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * 加载框
 */
public class LoadingUtils {

    private ProgressDialog mProgress;
    private Handler sMainHandler = new Handler(Looper.getMainLooper());
    
    public LoadingUtils(Context context){
        mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
    }
    
    public void setMessage(String message){
        mProgress.setMessage(message);
    }
    
    public void show(){
        try {
            if(mProgress != null){
                sMainHandler.post(new Runnable() {
                    
                    @Override
                    public void run() {
                        mProgress.show();
                    }
                });
            }
        } catch (Exception e) {
        }
    }
    
    public void dismiss(){
        if(mProgress != null && mProgress.isShowing()){
            sMainHandler.post(new Runnable() {
                
                @Override
                public void run() {
                    try {
                        mProgress.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
