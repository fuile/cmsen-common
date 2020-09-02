package com.cmsen.common.socket;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 套接字客户端测试 - 示例
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class SocketClientTest {
    private SocketClient client;

    public static void main(String[] args) {
        SocketClientTest socketClientTest = new SocketClientTest();
        socketClientTest.create();
        // 使用定时发送
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                socketClientTest.API();
            }
        }, 3000);
    }

    public void create() {
        // 创建一个客户端
        client = new SocketClient();
        // 设置连接服务端IP, 默认127.0.0.1
        client.setServerIp("127.0.0.1");
        // 设置连接服务端端口, 默认5209
        client.setServerPort(2000);
        // 设置阻塞模式, 默认非阻塞模式
        // client.setBlockingMode(true);
        // 设置持久的连接
        client.setKeepAlive(true);
        // 设置重置等待时间, 默认3000ms
        // client.setResetWaitTime(3000);
        // 设置发送缓冲区字节大小, 默认10个字节
        // client.setSendBufferSize(10);
        // 设置接收缓冲区字节大小, 默认8个字节
        // client.setReceiveBufferSize(8);
        // 设置消息队列大小, 默认100个
        // client.setMessageQueueSize(100);
        // 添加消息事件监听, 见SocketMessageTest类
        client.setSocketMessage(SocketMessageTest.class);
        // 打开客户端
        client.open();
    }

    // 模拟接口调用发送消息
    public void API() {
        // 消息队列 按照先进先出原则推送
        client.setMessage(SocketMessageTest.MESSAGES_INT)
                // 立即发送
                .send();
    }
}