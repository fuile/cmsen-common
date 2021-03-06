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
public class ClientHttp extends RequestConsts {
    public static ClientHttpResponse get(ClientHttpRequest httpRequest) {
        httpRequest.setMethod(GET).setProtocol(HTTP);
        return ClientHttpConnect.connect(httpRequest);
    }

    public static ClientHttpResponse post(ClientHttpRequest httpRequest) {
        httpRequest.setMethod(POST).setProtocol(HTTP);
        return ClientHttpConnect.connect(httpRequest);
    }

    public static ClientHttpResponse put(ClientHttpRequest httpRequest) {
        httpRequest.setMethod(PUT).setProtocol(HTTP);
        return ClientHttpConnect.connect(httpRequest);
    }

    public static ClientHttpResponse delete(ClientHttpRequest httpRequest) {
        httpRequest.setMethod(DELETE).setProtocol(HTTP);
        return ClientHttpConnect.connect(httpRequest);
    }

    public static ClientHttpResponse get(String url) {
        return get(url, null);
    }

    public static ClientHttpResponse get(String url, String params) {
        return get(url, params, null);
    }

    public static ClientHttpResponse get(String url, String params, Map<String, String> headers) {
        return get(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse post(String url) {
        return post(url, null);
    }

    public static ClientHttpResponse post(String url, String params) {
        return post(url, params, null);
    }

    public static ClientHttpResponse post(String url, String params, Map<String, String> headers) {
        return post(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse put(String url) {
        return put(url, null);
    }

    public static ClientHttpResponse put(String url, String params) {
        return put(url, params, null);
    }

    public static ClientHttpResponse put(String url, String params, Map<String, String> headers) {
        return put(new ClientHttpRequest(url, params, headers));
    }

    public static ClientHttpResponse delete(String url) {
        return delete(url, null);
    }

    public static ClientHttpResponse delete(String url, String params) {
        return delete(url, params, null);
    }

    public static ClientHttpResponse delete(String url, String params, Map<String, String> headers) {
        return delete(new ClientHttpRequest(url, params, headers));
    }
}
