/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

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
    private Map<String, String> headers;
    private StringBuilder body;

    public ClientHttpResponse() {
    }

    public ClientHttpResponse(int status) {
        this.status = status;
    }

    public ClientHttpResponse(String message, int status) {
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

    @Override
    public String toString() {
        return body.toString();
    }
}
