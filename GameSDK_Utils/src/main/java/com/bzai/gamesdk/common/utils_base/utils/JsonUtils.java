package com.bzai.gamesdk.common.utils_base.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by bzai on 2018/04/09.
 */

public class JsonUtils {


    /**
     * 格式化Json数据
     * @param jsonStr
     * @return
     */
    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for(int i=0;i<jsonStr.length();i++){
            char c = jsonStr.charAt(i);
            if(level>0&&'\n'==jsonForMatStr.charAt(jsonForMatStr.length()-1)){
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c+"\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c+"\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private  static String getLevelStr(int level){
        StringBuffer levelStr = new StringBuffer();
        for(int levelI = 0;levelI<level ; levelI++){
            levelStr.append("\t");
        }
        return levelStr.toString();
    }



    public static boolean isJson(String data){
        try{
            JSONObject dataJson = new JSONObject(data);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static HashMap<String,Object> jsonStrToMap(String jsonStr){

        HashMap<String, Object> mConfigs = new HashMap<>();
        if (!jsonStr.equals("")) {
            try {
                JSONObject jo = new JSONObject(jsonStr);
                Iterator<?> keys = jo.keys();
                while (keys.hasNext()) {
                    String key = keys.next().toString();
                    mConfigs.put(key, jo.getString(key));
                }
            } catch (JSONException e2) {
                LogUtils.e(e2.toString());
            }
        }
        return mConfigs;
    }


}
