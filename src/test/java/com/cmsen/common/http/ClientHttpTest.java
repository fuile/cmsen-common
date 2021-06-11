package com.cmsen.common.http;

import com.cmsen.common.util.StringUtil;
import junit.framework.TestCase;

import java.io.File;

public class ClientHttpTest extends TestCase {
    public void test() {

        ClientHttpRequest httpRequest = new ClientHttpRequest();
        httpRequest.setUrl("http://localhost:14556/api/upload/name");

        // httpRequest.setHeaders("Content-Type", "multipart/form-data");
        File file = new File("doc/image.png");
        File file2 = new File("doc/img_1.png");
        // {"id":"2","name":"cli"}

        httpRequest.setStreamBinary("name", "asdsadasdsad", "text/plain");
        httpRequest.setStreamBinary("json", "{\"id\":\"2\",\"name\":\"cli\"}", "application/json");
        httpRequest.setStreamBinary("file", "image1.jpg", file);
        httpRequest.setStreamBinary("file", "image2.jpg", file2);


        ClientHttpResponse response = ClientHttp.post(httpRequest);


        System.err.println(httpRequest.getHeaders());
        System.err.println(httpRequest.getStreamBinary());
        System.err.println(response);
        System.err.println(StringUtil.randomString(16));

    }
}