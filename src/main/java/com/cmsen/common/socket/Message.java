/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

public class Message {
    private SocketClient socketClientNonBlocking;

    public Message(SocketClient socketClientNonBlocking) {
        this.socketClientNonBlocking = socketClientNonBlocking;
    }

    public void send() {
        socketClientNonBlocking.send();
    }
}
