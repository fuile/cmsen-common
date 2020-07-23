package com.cmsen.common.http.response;

/**
 * 响应状态
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public enum ResponseStatus {
    SUCCESS(200, "SUCCESS"),

    FAILURE(500, "FAILURE"),

    BAD_REQUEST(400, "BAD_REQUEST"),

    UNAUTHORIZED(401, "UNAUTHORIZED"),

    FORBIDDEN(403, "FORBIDDEN"),

    NOT_FOUND(404, "NOT_FOUND"),

    UNSUPPORTED_MEDIA_TYPE(415, "UNSUPPORTED_MEDIA_TYPE"),

    SERVER_ERROR(500, "SERVER_ERROR"),

    BAD_GATEWAY(502, "BAD_GATEWAY");

    private int status;
    private String message;

    ResponseStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
