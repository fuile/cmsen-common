package com.cmsen.common.exception;

/**
 * 数据异常
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class DataException extends RuntimeException {
    private int code = 500;

    public int getCode() {
        return code;
    }

    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, int code) {
        super(message);
        this.code = code;
    }

    public DataException(String message, Object... msgArgs) {
        super(String.format(message, msgArgs));
    }

    public DataException(String message, int code, Object... msgArgs) {
        super(String.format(message, msgArgs));
        this.code = code;
    }

    public DataException(DataException e) {
        super(e.getMessage());
        this.code = e.getCode();
    }
}
