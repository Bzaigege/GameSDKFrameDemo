package com.bzai.gamesdk.common.utils_base.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bzai on 2018/04/09.
 *
 * 代码数据格式处理
 *
 */

public class CodeUtils {


    public static String Unicode2GBK(String dataStr) {
        int index = 0;
        StringBuffer buffer = new StringBuffer();

        int li_len = dataStr.length();
        while (index < li_len) {
            if (index >= li_len - 1
                    || !"\\u".equals(dataStr.substring(index, index + 2))) {
                buffer.append(dataStr.charAt(index));

                index++;
                continue;
            }

            String charStr = "";
            charStr = dataStr.substring(index + 2, index + 6);

            char letter = (char) Integer.parseInt(charStr, 16);

            buffer.append(letter);
            index += 6;
        }

        return buffer.toString();
    }


    /**
     * 网络请求对Map<>数据拼接处理   </>
     */
    public static String appendParams(StringBuilder builder, Map<String, ?> params){

        if (params == null || params.size() == 0) {
            return builder.toString();
        }

        Set<String> keys = params.keySet();
        String encoder = "UTF-8";
        try {
            boolean flag = true;
            for (String key : keys) {
                if (!flag) {
                    builder.append("&");
                }
                flag = false;
                Object value = params.get(key);
                builder.append(URLEncoder.encode(key, encoder));
                builder.append("=");
                builder.append(value != null ? URLEncoder.encode(value.toString(), encoder) : "");
            }

            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            return builder.toString();
        }
    }


    /**
     * 对传入的Kry值排序后，再拼接
     * (不需要做编码转换)
     */
    public static String sortAndAppendParams(StringBuilder builder, HashMap<String,Object> params){

        if (params == null || params.size() == 0) {
            return "";
        }

        Collection<String> keySet = params.keySet();
        List<String> list = new ArrayList<String>(keySet);

        //对key键值按字典升序排序
        Collections.sort(list);

//        String encoder = "UTF-8";
        try {
            boolean flag = true;
            for (String key : list) {
                if (!flag) {
                    builder.append("&");
                }
                flag = false;
                Object value = params.get(key);
//                builder.append(URLEncoder.encode(key, encoder));
                builder.append(key);
                builder.append("=");
                builder.append(value != null ? value.toString() : "");
//                builder.append(value != null ? URLEncoder.encode(value.toString(), encoder) : "");
            }

            return builder.toString();

        } catch (Exception e) {

            return "";
        }

    }

}
