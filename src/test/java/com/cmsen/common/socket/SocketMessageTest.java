/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import com.cmsen.common.util.ByteUtil;

import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 套接字消息测试 - 示例
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class SocketMessageTest implements SocketMessage {
    /**
     * 监听端口
     */
    public static int PORT = 2000;

    /**
     * 监听主机
     */
    public static String HOST = "127.0.0.1";

    /**
     * 消息正文
     * 2521234253 = [00 FC 00 01 00 02 00 03 00 FD]
     */
    public static int[] MESSAGES_INT = {252, 1, 2, 3, 253};


    public SocketMessageTest() {
    }

    @Override
    public long heartRate() {
        // 0-关闭心跳检测
        return 3000;
    }

    @Override
    public int heartRateData() {
        return 0xff;
    }

    @Override
    public boolean heartRateError(Exception e) {
        // true=启动自动重连
        return true;
    }

    @Override
    public void finishConnect(Socket socket) {
        // 连接完成时
    }

    @Override
    public boolean connectError(Exception e) {
        // true=启动自动重连
        return true;
    }

    @Override
    public void send(ByteBuffer buffer) throws Exception {
        System.err.println(String.format("Send message: [ length: %d, hex: %s]", buffer.limit(), ByteUtil.toHex(buffer.array())));
    }

    @Override
    public boolean sendError(Exception e) {
        System.err.println("Sending message error: " + e.getMessage());
        return true;
    }

    @Override
    public void receive(ByteBuffer buffer) {
        System.err.println(String.format("Received message: [ length: %d, hex: %s]", buffer.limit(), ByteUtil.toHex(buffer.array())));
    }

    @Override
    public boolean receiveError(Exception e) {
        System.err.println("Receiving message error: " + e.getMessage());
        return false;
    }
}
