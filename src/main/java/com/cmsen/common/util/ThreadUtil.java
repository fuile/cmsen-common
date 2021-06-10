/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

public class ThreadUtil {
    public static Thread run(Runnable target) {
        return new Thread(target);
    }

    public static Thread run(Runnable target, String threadName) {
        Thread thread = new Thread(target);
        thread.setName(threadName);
        return thread;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
