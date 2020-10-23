/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.exception;

public abstract class ExceptionAdviceHandler<T> {
    public abstract T serviceException(ServiceException e);

    public abstract T dataException(DataException e);

    public abstract T databaseException(DatabaseException e);

    public abstract T runtimeException(RuntimeException e);

    public abstract T exception(Exception e);
}
