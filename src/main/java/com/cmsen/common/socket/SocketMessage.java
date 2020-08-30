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
import java.util.List;

public interface SocketMessage {
    default long heartRate() {
        return 3000;
    }

    default int heartRateData() {
        return 0xff;
    }

    default void heartRateDataError(IOException e) {
        System.err.println(String.format("%s, %dms after, %s", e.getMessage(), heartRate(), "Automatic reconnection..."));
    }

    default void onReadMessageError(IOException e) {
    }

    default void onSendMessageError(IOException e, List<byte[]> message) {
    }

    default void onConnectSuccess(SocketThread socketThread, Socket socket) {
        System.out.println("Connection established: " + socket);
    }

    default boolean onConnectFailed(IOException e) {
        System.err.println(String.format("%s, %s", e.getMessage(), "Automatic reconnection..."));
        return true;
    }

    void onReadMessage(DataInputStream reader) throws IOException;


    void onSendMessage(DataOutputStream writer, byte[] message) throws IOException;

}
