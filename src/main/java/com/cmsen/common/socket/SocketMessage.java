/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public interface SocketMessage {

    default long heartRate() {
        return 3000;
    }

    default int heartRateData() {
        return 0xff;
    }

    default void heartRateDataError(IOException e) {
    }

    default void onReadMessageError(IOException e) {
    }

    default boolean onSendMessageError(IOException e, Queue<byte[]> message) {
        return false;
    }

    default void onConnectSuccess(SocketThread socketThread, Socket socket) {
    }

    default boolean onConnectFailed(IOException e) {
        return true;
    }

    void onReadMessage(DataInputStream reader) throws IOException;


    void onSendMessage(DataOutputStream writer, byte[] message) throws IOException;

    default void onSendMessageBefore(Queue<byte[]> message, boolean terminated) {
    }
}
