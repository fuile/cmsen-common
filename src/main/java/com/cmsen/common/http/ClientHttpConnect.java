/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class ClientHttpConnect {
    public static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

    public static ClientHttpResponse connect(ClientHttpRequest httpRequest) {
        if (httpRequest.isProtocolSSL()) {
            return https(httpRequest);
        }
        return http(httpRequest);
    }

    public static ClientHttpResponse http(ClientHttpRequest httpRequest) {
        if (httpRequest.isProtocolSSL()) {
            throw new IllegalArgumentException("Request protocol not supported");
        }
        ClientHttpResponse httpResponse = new ClientHttpResponse();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) httpRequest.openConnection().openConnection();
            connection.setRequestMethod(httpRequest.getMethod());
            connection.setDoInput(true);
            connection.setDoOutput(httpRequest.isParamsXorStream());
            connection.setUseCaches(false);
            connection.setConnectTimeout(httpRequest.getTimeOut());
            connection.setReadTimeout(httpRequest.getTimeOut());
            connection.setInstanceFollowRedirects(httpRequest.isFollowRedirect());
            setRequestHeaders(connection, httpRequest);
            connection.connect();
            if (!httpRequest.getMethod().equals("GET")) {
                if (httpRequest.isParamsXorStream()) {
                    OutputStream os = connection.getOutputStream();
                    if (null != httpRequest.getParams()) {
                        byte[] params = httpRequest.getParams(StandardCharsets.UTF_8);
                        os.write(params);
                    } else if (null != httpRequest.getStream()) {
                        os.write(httpRequest.getStream());
                    }
                    os.flush();
                    os.close();
                }
            }
            int responseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            if (responseCode >= HttpsURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getErrorStream();
            }
            httpResponse.setStatus(responseCode);
            httpResponse.setMessage(connection.getResponseMessage());
            httpResponse.setHeaders(getResponseHeader(httpResponse, connection));
            httpResponse.setBody(getResponseBody(inputStream));
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("ClientHttpConnect: " + e.getMessage());
            if (null != connection) {
                connection.disconnect();
            }
            httpResponse = new ClientHttpResponse(e.getMessage(), 500);
        }
        return httpResponse;
    }

    public static ClientHttpResponse https(ClientHttpRequest httpRequest) {
        if (!httpRequest.isProtocolSSL()) {
            throw new IllegalArgumentException("Request protocol not supported");
        }
        ClientHttpResponse httpResponse = new ClientHttpResponse();
        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) httpRequest.openConnection().openConnection();
            connection.setSSLSocketFactory(ClientHttpCertificate.sslContext().getSocketFactory());
            connection.setRequestMethod(httpRequest.getMethod());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setConnectTimeout(httpRequest.getTimeOut());
            connection.setReadTimeout(httpRequest.getTimeOut());
            connection.setInstanceFollowRedirects(httpRequest.isFollowRedirect());
            setRequestHeaders(connection, httpRequest);
            connection.connect();
            if (!httpRequest.getMethod().equals("GET")) {
                if (httpRequest.isParamsXorStream()) {
                    OutputStream os = connection.getOutputStream();
                    if (null != httpRequest.getParams()) {
                        byte[] params = httpRequest.getParams(StandardCharsets.UTF_8);
                        os.write(params);
                    } else if (null != httpRequest.getStream()) {
                        os.write(httpRequest.getStream());
                    }
                    os.flush();
                    os.close();
                }
            }
            int responseCode = connection.getResponseCode();
            InputStream inputStream = connection.getInputStream();
            if (responseCode >= HttpsURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getErrorStream();
            }
            httpResponse.setStatus(responseCode);
            httpResponse.setMessage(connection.getResponseMessage());
            httpResponse.setHeaders(getResponseHeader(httpResponse, connection));
            httpResponse.setBody(getResponseBody(inputStream));
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("ClientHttpsConnect: " + e.getMessage());
            if (null != connection) {
                connection.disconnect();
            }
            httpResponse = new ClientHttpResponse(e.getMessage(), 500);
        }
        return httpResponse;
    }

    protected static StringBuilder getResponseBody(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        return result;
    }

    protected static Map<String, String> getResponseHeader(ClientHttpResponse httpResponse, URLConnection connection) {
        Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if ("Set-Cookie".equalsIgnoreCase(header.getKey())) {
                httpResponse.setCookies(header.getValue());
                continue;
            }
            headers.put(header.getKey(), header.getValue().get(0));
        }
        return headers;
    }

    protected static void setRequestHeaders(URLConnection connection, ClientHttpRequest httpRequest) {
        connection.setRequestProperty("Host", httpRequest.getUrl().getAuthority());
        connection.setRequestProperty("User-Agent", userAgent);
        connection.setRequestProperty("Connection", "Keep-Alive");
        if (null != httpRequest.getParams()) {
            connection.setRequestProperty("Content-Length", String.valueOf(httpRequest.getParams(StandardCharsets.UTF_8).length));
        } else if (null != httpRequest.getStream()) {
            connection.setRequestProperty("Content-Length", String.valueOf(httpRequest.getStream().length));
        }
        if (httpRequest.getMethod().equals("POST")) {
            connection.setRequestProperty("Content-Type", ContentEnctype.URLENCODED);
        }
        if (null != httpRequest.getHeaders()) {
            for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        if (null != httpRequest.getCookies()) {
            connection.setRequestProperty("Cookie", httpRequest.getCookie());
        }
    }
}
