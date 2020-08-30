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

public interface SocketMessage {
    default long heartRate() {
        return 3000;
    }

    default int heartRateData() {
        return 0xff;
    }

    void onReadMessage(DataInputStream reader) throws IOException;

    void onSendMessage(DataOutputStream writer, byte[] message) throws IOException;
}
