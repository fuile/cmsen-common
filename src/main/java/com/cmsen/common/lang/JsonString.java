/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.lang;

import com.cmsen.common.util.JSON;

public class JsonString<T> {
    private final T object;

    public JsonString(String value) {
        this.object = JSON.parse(value);
    }

    public T getObject() {
        return object;
    }

    public boolean isEmpty() {
        return object == null;
    }

    @Override
    public String toString() {
        return !isEmpty() ? object.toString() : null;
    }
}
