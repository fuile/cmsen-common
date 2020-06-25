package com.cmsen.common.exception;

/**
 * 数据库异常
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class DatabaseException extends ServiceException {
    private int code = 500;

    public int getCode() {
        return code;
    }

    public DatabaseException() {
        super();
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, int code) {
        super(message);
        this.code = code;
    }
}
