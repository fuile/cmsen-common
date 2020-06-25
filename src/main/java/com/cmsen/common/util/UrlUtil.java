/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class UrlUtil {
    /**
     * 字符串化参数
     *
     * @param params 参数
     * @return a=b&c=d
     */
    public static String parseParams(Map<String, Object> params) {
        int i = 1;
        int size = params.size();
        StringBuilder paramMap = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            paramMap.append(param.getKey());
            paramMap.append("=");
            paramMap.append(param.getValue());
            if (i < size) {
                paramMap.append("&");
            }
            i++;
        }
        return paramMap.toString();
    }

    /**
     * 对象化参数
     *
     * @param params 参数
     * @return Map对象
     */
    public static Map<String, Object> parseParams(String params) {
        String[] strings = params.split("&");
        Map<String, Object> param = new HashMap<String, Object>();
        for (int i = 0; i < strings.length; i++) {
            String[] string = strings[i].split("=", 2);
            param.put(string[0], string.length > 1 ? string[1] : "");
        }
        return param;
    }

    public static String encode(String s) {
        return encode(s, "UTF-8");
    }

    public static String encode(String s, String enc) {
        try {
            return URLEncoder.encode(s, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
        }
        return s;
    }

    public static String decode(String s) {
        return decode(s, "UTF-8");
    }

    public static String decode(String s, String enc) {
        try {
            return URLDecoder.decode(s, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
        }
        return s;
    }
}
