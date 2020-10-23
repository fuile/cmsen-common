/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.exception;

@FunctionalInterface
public interface ProcessingTransactional<T> {
    T run();

    static <T> T insert(ProcessingTransactional<T> processingTransactional) {
        return execute(processingTransactional, "Creating failed");
    }

    static <T> T update(ProcessingTransactional<T> processingTransactional) {
        return execute(processingTransactional, "Updating failed");
    }

    static <T> T delete(ProcessingTransactional<T> processingTransactional) {
        return execute(processingTransactional, "Deleting failed");
    }

    static <T> T execute(ProcessingTransactional<T> processingTransactional, String message) {
        return execute(processingTransactional, message, 500);
    }

    static <T> T execute(ProcessingTransactional<T> processingTransactional, String message, int code) {
        try {
            return processingTransactional.run();
        } catch (ServiceException e) {
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new DatabaseException(message, code);
        }
    }
}
