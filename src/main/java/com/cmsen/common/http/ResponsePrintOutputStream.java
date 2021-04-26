/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http;

import java.io.IOException;
import java.io.InputStream;

public interface ResponsePrintOutputStream {
    boolean print(InputStream inputStream, ClientHttpResponse response) throws IOException;
}
