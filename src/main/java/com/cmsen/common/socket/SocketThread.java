/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SocketThread {
    private ExecutorService executorService;
    private SocketMessage socketMessage;

    public SocketThread(SocketMessage socketMessage) {
        this.executorService = Executors.newCachedThreadPool();
        this.socketMessage = socketMessage;
    }

    public SocketThread(int num, SocketMessage socketMessage) {
        this.executorService = Executors.newFixedThreadPool(num);
        this.socketMessage = socketMessage;
    }

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public static Thread create(Runnable target) {
        return new Thread(target);
    }

    public void run(Runnable target) {
        executorService.submit(target);
    }

    public void shutdown() {
        if (null != executorService && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    public void stop() {
        if (null != executorService) {
            try {
                executorService.shutdown();
                if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException interruptedException) {
                executorService.shutdownNow();
            }
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
