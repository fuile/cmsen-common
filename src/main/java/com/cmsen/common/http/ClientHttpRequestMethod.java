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
public abstract class ClientHttpRequestMethod {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static ClientHttpResponse get(ClientHttpRequest httpRequest) {
        return ClientHttpConnect.connect(httpRequest.setMethod(GET));
    }

    public static ClientHttpResponse post(ClientHttpRequest httpRequest) {
        return ClientHttpConnect.connect(httpRequest.setMethod(POST));
    }

    public static ClientHttpResponse put(ClientHttpRequest httpRequest) {
        return ClientHttpConnect.connect(httpRequest.setMethod(PUT));
    }

    public static ClientHttpResponse delete(ClientHttpRequest httpRequest) {
        return ClientHttpConnect.connect(httpRequest.setMethod(DELETE));
    }

    public static ClientHttpResponse get(String url) {
        return get(url, null);
    }

    public static ClientHttpResponse get(String url, String params) {
        return get(url, params, null);
    }

    public static ClientHttpResponse get(String url, String params, Map<String, Object> headers) {
        return get(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse post(String url) {
        return post(url, null);
    }

    public static ClientHttpResponse post(String url, String params) {
        return post(url, params, null);
    }

    public static ClientHttpResponse post(String url, String params, Map<String, Object> headers) {
        return post(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse put(String url) {
        return put(url, null);
    }

    public static ClientHttpResponse put(String url, String params) {
        return put(url, params, null);
    }

    public static ClientHttpResponse put(String url, String params, Map<String, Object> headers) {
        return put(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse delete(String url) {
        return delete(url, null);
    }

    public static ClientHttpResponse delete(String url, String params) {
        return delete(url, params, null);
    }

    public static ClientHttpResponse delete(String url, String params, Map<String, Object> headers) {
        return delete(new ClientHttpRequest(url, params, headers));
    }
}
