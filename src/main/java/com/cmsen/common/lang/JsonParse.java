/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.lang;

import com.cmsen.common.util.JSON;

public abstract class JsonParse<T> {
    protected T data;

    protected JsonParse(String value) {
        this.data = JSON.parse(value);
    }

    public T getData() {
        return data;
    }
}
