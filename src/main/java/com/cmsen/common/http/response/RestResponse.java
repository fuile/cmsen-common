package com.cmsen.common.http.response;

import com.cmsen.common.http.pagination.Pagination;

/**
 * 包装REST请求响应
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public interface RestResponse {

    static RestResponse success() {
        return new Response(ResponseStatus.SUCCESS);
    }

    static <T> RestResponse success(T result) {
        return new Response.Success<>(result);
    }

    static <T> RestResponse success(T result, int status) {
        return new Response.Success<>(result, status);
    }

    static <T> RestResponse success(T result, String message) {
        return new Response.Success<>(result, message);
    }

    static <T> RestResponse success(T result, String message, int status) {
        return new Response.Success<>(result, message, status);
    }

    static <T> RestResponse success(T result, ResponseStatus responseStatus) {
        return new Response.Success<>(result, responseStatus);
    }

    static <T> RestResponse success(T result, Pagination page) {
        return new Response.Page<>(result, page);
    }

    static <T> RestResponse success(T result, Pagination page, int status) {
        return new Response.Page<>(result, page, status);
    }

    static <T> RestResponse success(T result, Pagination page, String message) {
        return new Response.Page<>(result, page, message);
    }

    static <T> RestResponse success(T result, Pagination page, ResponseStatus responseStatus) {
        return new Response.Page<>(result, page, responseStatus);
    }

    static <T> RestResponse success(T result, Pagination page, String message, int status) {
        return new Response.Page<>(result, page, message, status);
    }

    static RestResponse failure() {
        return new Response();
    }

    static RestResponse failure(int status) {
        return new Response(status);
    }

    static RestResponse failure(String message) {
        return new Response(message);
    }

    static RestResponse failure(String message, int status) {
        return new Response(message, status);
    }

    static RestResponse failure(ResponseStatus responseStatus) {
        return new Response(responseStatus);
    }
}
