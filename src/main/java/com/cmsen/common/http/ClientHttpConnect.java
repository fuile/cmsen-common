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
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class ClientHttpConnect {
    public static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

    public static ClientHttpResponse connect(ClientHttpRequest httpRequest) {
        int status = 500;
        String message = null;
        try {
            if (httpRequest.isProtocolSSL()) {
                return https(httpRequest);
            }
            return http(httpRequest);
        } catch (MalformedURLException e) {
            message = "URL format error";
            e.printStackTrace(System.err);
        } catch (ConnectException e) {
            status = 402;
            message = "Internet connection failed";
            e.printStackTrace(System.err);
        } catch (UnknownHostException e) {
            status = 404;
            message = "Target is inaccessible";
            e.printStackTrace(System.err);
        } catch (Exception e) {
            message = e.getMessage();
            e.printStackTrace(System.err);
        }
        return new ClientHttpResponse(message, status);
    }

    public static ClientHttpResponse http(ClientHttpRequest httpRequest) throws IOException {
        if (httpRequest.isProtocolSSL()) {
            throw new IllegalArgumentException("Request protocol not supported");
        }
        HttpURLConnection connection = (HttpURLConnection) httpRequest.openConnection();
        connection.setRequestMethod(httpRequest.getMethod());
        connection.setDoInput(true);
        connection.setDoOutput(httpRequest.isParamsXorStream());
        connection.setUseCaches(false);
        connection.setConnectTimeout(httpRequest.getTimeOut());
        setRequestHeaders(connection, httpRequest);
        if (!httpRequest.getMethod().equals("GET")) {
            setRequestParams(connection, httpRequest);
        }
        connection.connect();
        ClientHttpResponse httpResponse = new ClientHttpResponse();
        httpResponse.setStatus(connection.getResponseCode());
        httpResponse.setMessage(connection.getResponseMessage());
        httpResponse.setHeaders(getResponseHeader(httpResponse, connection));
        httpResponse.setBody(getResponseBody(httpResponse.isSuccess() ? connection.getInputStream() : connection.getErrorStream()));
        connection.disconnect();
        return httpResponse;
    }

    public static ClientHttpResponse https(ClientHttpRequest httpRequest) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        if (!httpRequest.isProtocolSSL()) {
            throw new IllegalArgumentException("Request protocol not supported");
        }
        HttpsURLConnection connection = (HttpsURLConnection) httpRequest.openConnection();
        connection.setSSLSocketFactory(ClientHttpCertificate.sslContext().getSocketFactory());
        connection.setRequestMethod(httpRequest.getMethod());
        connection.setDoInput(true);
        connection.setDoOutput(httpRequest.isParamsXorStream());
        connection.setUseCaches(false);
        connection.setConnectTimeout(httpRequest.getTimeOut());
        setRequestHeaders(connection, httpRequest);
        if (!httpRequest.getMethod().equals("GET")) {
            setRequestParams(connection, httpRequest);
        }
        connection.connect();
        ClientHttpResponse httpResponse = new ClientHttpResponse();
        httpResponse.setStatus(connection.getResponseCode());
        httpResponse.setMessage(connection.getResponseMessage());
        httpResponse.setHeaders(getResponseHeader(httpResponse, connection));
        httpResponse.setBody(getResponseBody(httpResponse.isSuccess() ? connection.getInputStream() : connection.getErrorStream()));
        connection.disconnect();
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

    protected static void setRequestParams(URLConnection connection, ClientHttpRequest httpRequest) throws IOException {
        if (null != httpRequest && httpRequest.isParamsXorStream()) {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            if (null != httpRequest.getParams()) {
                connection.setRequestProperty("Content-Length", "" + httpRequest.getParams(StandardCharsets.UTF_8).length);
                outputStream.write(httpRequest.getParams(StandardCharsets.UTF_8));
            } else if (null != httpRequest.getStream()) {
                outputStream.write(httpRequest.getStream());
            }
            outputStream.flush();
            outputStream.close();
        }
    }
}
