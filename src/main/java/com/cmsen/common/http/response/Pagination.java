/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.http.response;

public interface Pagination {
    default int pageNumber(int pageNumber, int def) {
        return Math.max(pageNumber, def);
    }

    default int pageSize(int pageSize, int def) {
        return Math.max(pageSize, def);
    }

    default int pageTotal(int total, int pageSize) {
        return (int) Math.ceil(1.0 * total / pageSize);
    }

    default int startIndex(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

    default int endIndex(int pageNumber, int pageSize) {
        return pageNumber * pageSize;
    }
}
