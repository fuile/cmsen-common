/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

import com.cmsen.common.util.UrlUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class ClientHttpRequest {
    private String url;
    private String protocol = "http";
    private String host;
    private int port = -1;
    private int timeOut = 30000;
    private boolean followRedirect = false;
    private String path = "";
    private String method = "GET";
    private String params;
    private byte[] stream;
    private Map<String, String> cookies;
    private Map<String, String> headers;
    private Map<String, Object> paramsMap;

    private ResponsePrintOutputStream printOutputStream;

    public ClientHttpRequest() {
    }

    public ClientHttpRequest(String url) {
        setUrl(url);
    }

    public ClientHttpRequest(String url, String params, Map<String, String> headers) {
        setUrl(url).setParams(params).setHeaders(headers);
    }

    public ClientHttpRequest(String host, String path) {
        this(null, host, -1, path);
    }

    public ClientHttpRequest(String protocol, String host, int port) {
        this(protocol, host, port, null);
    }

    public ClientHttpRequest(String protocol, String host, int port, String path) {
        setProtocol(protocol);
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public ClientHttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public URL getUrl() {
        try {
            return getUrlConnect(url);
        } catch (MalformedURLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public URL getUrlConnect(String url) throws MalformedURLException {
        if (null == url) {
            if (getMethod().equals("GET") && null != getParams()) {
                path += (path.indexOf("?") > 0 ? "&" : "?") + getParams();
            }
            return new URL(protocol, host, port, path);
        }
        if (getMethod().equals("GET") && null != getParams()) {
            url += (url.indexOf("?") > 0 ? "&" : "?") + getParams();
        }
        return new URL(url);
    }

    public ClientHttpRequest setHost(String host) {
        this.host = host;
        return this;
    }

    public ClientHttpRequest setPort(int port) {
        this.port = port;
        return this;
    }

    public boolean isProtocolSSL() {
        return "https".equals(getUrl().getProtocol());
    }

    public ClientHttpRequest setProtocol(String protocol) {
        this.protocol = protocol.toLowerCase();
        return this;
    }

    public ClientHttpRequest setPath(String path) {
        this.path = path;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ClientHttpRequest setMethod(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public String getParams() {
        return null == paramsMap ? params : UrlUtil.parseParams(paramsMap);
    }

    public byte[] getParams(Charset charset) {
        return getParams().getBytes(charset);
    }

    public ClientHttpRequest setParams(String params) {
        this.params = params;
        return this;
    }

    public ClientHttpRequest setParams(Map<String, Object> params) {
        this.paramsMap = params;
        return this;
    }

    public ClientHttpRequest setParams(String Key, Object value) {
        if (null == this.paramsMap) {
            this.paramsMap = new HashMap<>();
        }
        this.paramsMap.put(Key, value);
        return this;
    }

    public byte[] getStream() {
        return stream;
    }

    public ClientHttpRequest setStream(byte[] stream) {
        this.stream = stream;
        return this;
    }

    public boolean isParamsXorStream() {
        return (null != paramsMap || null != params) || null != stream;
    }

    public String getCookie() {
        StringBuilder sb = new StringBuilder();
        int s = cookies.size();
        int i = 0;
        for (Map.Entry<String, String> cookie : cookies.entrySet()) {
            sb.append(cookie.getKey());
            sb.append("=");
            sb.append(cookie.getValue());
            if (i < s) {
                sb.append(";");
            }
            i++;
        }
        return sb.toString();
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public ClientHttpRequest setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    public ClientHttpRequest setCookies(String Key, String value) {
        if (null == this.cookies) {
            this.cookies = new HashMap<>();
        }
        this.cookies.put(Key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String Key) {
        return null == this.headers ? null : headers.get(Key);
    }

    public ClientHttpRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ClientHttpRequest setHeaders(String Key, String value) {
        if (null == this.headers) {
            this.headers = new HashMap<>();
        }
        this.headers.put(Key, value);
        return this;
    }

    public URL openConnection() throws IOException {
        return getUrlConnect(url);
    }

    public int getTimeOut() {
        return timeOut;
    }

    public ClientHttpRequest setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public boolean isFollowRedirect() {
        return followRedirect;
    }

    public ClientHttpRequest setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }

    public ResponsePrintOutputStream getPrintOutputStream() {
        return printOutputStream;
    }

    public void setPrintOutputStream(ResponsePrintOutputStream printOutputStream) {
        this.printOutputStream = printOutputStream;
    }

    @Override
    public String toString() {
        return getUrl().toString();
    }
}
