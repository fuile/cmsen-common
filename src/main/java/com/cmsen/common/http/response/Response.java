package com.cmsen.common.http.response;


/**
 * 包装REST响应
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class Response implements RestResponse {
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response() {
        this(ResponseStatus.FAILURE);
    }

    public Response(ResponseStatus responseCode) {
        this(responseCode.getMessage(), responseCode.getStatus());
    }

    public Response(int status) {
        this(ResponseStatus.FAILURE.getMessage(), status);
    }

    public Response(String message) {
        this(message, ResponseStatus.FAILURE.getStatus());
    }

    public Response(String message, int status) {
        this.status = status;
        this.message = message;
    }

    public static class Success<T> implements RestResponse {
        private int status;
        private String message;
        private T result;

        public int getStatus() {
            return status;
        }

        public Success<T> setStatus(int status) {
            this.status = status;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Success<T> setMessage(String message) {
            this.message = message;
            return this;
        }

        public T getResult() {
            return result;
        }

        public Success<T> setResult(T result) {
            this.result = result;
            return this;
        }

        public Success(T result) {
            this(result, ResponseStatus.SUCCESS);
        }

        public Success(T result, ResponseStatus responseCode) {
            this(result, responseCode.getMessage(), responseCode.getStatus());
        }

        public Success(T result, int status) {
            this(result, ResponseStatus.SUCCESS.getMessage(), status);
        }

        public Success(T result, String message) {
            this(result, message, ResponseStatus.SUCCESS.getStatus());
        }

        public Success(T result, String message, int status) {
            this.result = result;
            this.status = status;
            this.message = message;
        }
    }

    public static class Page<T> implements RestResponse {
        private int status;
        private String message;
        private T result;
        private Pagination page;

        public int getStatus() {
            return status;
        }

        public Page<T> setStatus(int status) {
            this.status = status;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Page<T> setMessage(String message) {
            this.message = message;
            return this;
        }

        public T getResult() {
            return result;
        }

        public Page<T> setResult(T result) {
            this.result = result;
            return this;
        }

        public Pagination getPage() {
            return page;
        }

        public Page<T> setPage(Pagination page) {
            this.page = page;
            return this;
        }

        public Page(T result, Pagination page) {
            this(result, page, ResponseStatus.SUCCESS.getMessage());
        }

        public Page(T result, Pagination page, int status) {
            this(result, page, ResponseStatus.SUCCESS.getMessage(), status);
        }

        public Page(T result, Pagination page, String message) {
            this(result, page, message, ResponseStatus.SUCCESS.getStatus());
        }

        public Page(T result, Pagination page, ResponseStatus responseCode) {
            this(result, page, responseCode.getMessage(), responseCode.getStatus());
        }

        public Page(T result, Pagination page, String message, int status) {
            this.result = result;
            this.page = page;
            this.status = status;
            this.message = message;
        }
    }
}
