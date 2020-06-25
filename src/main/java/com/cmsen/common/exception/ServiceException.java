package com.cmsen.common.exception;

/**
 * 服务异常
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class ServiceException extends RuntimeException {
    private int code = 500;

    public int getCode() {
        return code;
    }

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message, Object... msgArgs) {
        super(String.format(message, msgArgs));
    }

    public ServiceException(String message, int code, Object... msgArgs) {
        super(String.format(message, msgArgs));
        this.code = code;
    }

    public ServiceException(ServiceException e) {
        super(e.getMessage());
        this.code = e.getCode();
    }
}
