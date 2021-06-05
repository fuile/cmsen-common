/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class ClientHttpResponse {
    /**
     * 响应状态码
     */
    private int status;
    private String message;
    /**
     * 响应头
     */
    private Map<String, String> headers = new HashMap<>();
    private List<String> cookies = new ArrayList<>();
    private StringBuilder body;
    private ObjectMapper mapper = new ObjectMapper();

    public ClientHttpResponse() {
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ClientHttpResponse(int status) {
        this();
        this.status = status;
    }

    public ClientHttpResponse(String message, int status) {
        this();
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public ClientHttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public ClientHttpResponse setStatus(String message, int status) {
        this.message = message;
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ClientHttpResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCookie() {
        StringBuilder sb = new StringBuilder();
        int s = cookies.size();
        int i = 0;
        for (int i1 = 0; i1 < cookies.size(); i1++) {
            sb.append(cookies.get(i));
            if (i < s) {
                sb.append(";");
            }
            i++;
        }
        return sb.toString();
    }

    public String getCookie(int i) {
        return cookies.get(i);
    }

    public List<String> getCookies() {
        return cookies;
    }

    public ClientHttpResponse setCookies(List<String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 获取给定请求头值
     *
     * @param name 请求头名
     * @return 请求头值
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    public ClientHttpResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public StringBuilder getBody() {
        return body;
    }

    public ClientHttpResponse setBody(StringBuilder body) {
        this.body = body;
        return this;
    }

    /**
     * 判断请求响应是否是200成功
     */
    public boolean isSuccess() {
        return this.status == 200;
    }

    public boolean isSuccess(int status) {
        return this.status == status;
    }

    /**
     * 判断请求响应是否是301、302重定向
     */
    public boolean isRedirect() {
        return this.status == 301 || this.status == 302;
    }

    public <T> T getForObject() {
        return getForObject(false, null);
    }

    public <T> T getForObject(boolean nonNull) {
        return getForObject(nonNull, null);
    }

    public <T> T getForObject(boolean nonNull, T def) {
        try {
            if (nonNull) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            return mapper.readValue(toString(), new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return def;
    }

    public <T> T getForObject(Class<T> valueType) {
        return getForObject(valueType, false, null);
    }

    public <T> T getForObject(Class<T> valueType, boolean nonNull) {
        return getForObject(valueType, nonNull, null);
    }

    public <T> T getForObject(Class<T> valueType, boolean nonNull, T def) {
        try {
            if (nonNull) {
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            return mapper.readValue(toString(), valueType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return def;
    }

    @Override
    public String toString() {
        return null == body ? "" : body.toString();
    }
}
