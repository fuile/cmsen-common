/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import com.cmsen.common.util.ByteUtil;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * 套接字客户端
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class SocketClient {
    private final Logger log = Logger.getLogger(SocketClient.class.getName());
    /**
     * 监听主机
     */
    private String host;
    /**
     * 监听端口
     */
    private int port;
    /**
     * 读取超时时长
     */
    private int timeOut;
    /**
     * 消息队列 先进先出原则
     */
    private static Queue<byte[]> message = new LinkedList<>();
    /**
     * 套接字消息
     */
    private SocketMessage socketMessage;
    /**
     * 是否保持
     */
    private boolean keepAlive;
    /**
     * 是否结束
     */
    private boolean terminated;
    /**
     * 是否连接
     */
    private boolean connect;

    /**
     * 构造方法
     */
    public SocketClient() {
    }

    /**
     * 构造方法
     *
     * @param host 监听主机
     * @param port 监听端口
     */
    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 构造方法
     *
     * @param host    监听主机
     * @param port    监听端口
     * @param timeOut 读取超时时长
     */
    public SocketClient(String host, int port, int timeOut) {
        this(host, port);
        this.timeOut = timeOut;
    }

    /**
     * 构造方法
     *
     * @param host      监听主机
     * @param port      监听端口
     * @param keepAlive 是否保持
     */
    public SocketClient(String host, int port, boolean keepAlive) {
        this(host, port);
        this.keepAlive = keepAlive;
    }

    /**
     * 构造方法
     *
     * @param host    监听主机
     * @param port    监听端口
     * @param message 消息队列 先进先出原则
     */
    public SocketClient(String host, int port, Queue<byte[]> message) {
        this(host, port);
        this.message = message;
    }

    /**
     * 构造方法
     *
     * @param host          监听主机
     * @param port          监听端口
     * @param socketMessage 套接字消息
     */
    public SocketClient(String host, int port, SocketMessage socketMessage) {
        this(host, port);
        this.socketMessage = socketMessage;
    }

    public String getHost() {
        return host;
    }

    public SocketClient setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SocketClient setPort(int port) {
        this.port = port;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public SocketClient setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Queue<byte[]> getMessages() {
        return message;
    }

    public byte[] getMessage(boolean isDelete) {
        return !isDelete ? message.peek() : message.poll();
    }

    public SocketClient setMessage(String message) {
        if (!this.message.offer(message.getBytes())) {
            log.severe("setMessage fail: " + message);
        }
        return this;
    }

    public SocketClient setMessage(byte[] message) {
        if (!this.message.offer(message)) {
            log.severe("setMessage fail: " + message);
        }
        return this;
    }

    public SocketClient setMessage(byte[]... message) {
        if (!this.message.offer(ByteUtil.merger(message))) {
            log.severe("setMessage fail: " + Arrays.toString(message));
        }
        return this;
    }

    public SocketClient setMessage(int... data) {
        if (!this.message.offer(ByteUtil.toBytes2(data))) {
            log.severe("setMessage fail: " + Arrays.toString(data));
        }
        return this;
    }

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

    /**
     * 添加消息侦听器
     *
     * @param socketMessage 套接字消息
     * @return
     */
    public SocketClient addMessageListener(SocketMessage socketMessage) {
        if (connect) {
            throw new IllegalStateException("addMessageListener must on connection() before");
        }
        this.socketMessage = socketMessage;
        return this;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public SocketClient setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public boolean isConnect() {
        return connect;
    }

    /**
     * 断开连接
     *
     * @param socket 套接字对象
     * @return
     */
    public void disconnect(Socket socket) {
        if (null != socket && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 连接
     *
     * @return
     */
    public void connection() {
        connection(host, port);
    }

    /**
     * 连接
     *
     * @param socketMessage 套接字消息
     * @return
     */
    public void connection(SocketMessage socketMessage) {
        connection(host, port, socketMessage);
    }

    /**
     * 连接
     *
     * @param host 监听主机
     * @param port 监听端口
     * @return
     */
    public void connection(String host, int port) {
        connection(host, port, socketMessage);
    }

    /**
     * 连接
     *
     * @param host 监听主机
     * @param port 监听端口
     * @return
     */
    public void connection(String host, int port, SocketMessage socketMessage) {
        connect = true;
        if (null == host) {
            throw new IllegalArgumentException("connect: Address is invalid on local machine");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("connect: port is not valid on remote machine");
        }
        SocketThread socketThread = new SocketThread(3, socketMessage);
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            socket.setKeepAlive(keepAlive);
            socket.setSoTimeout(timeOut);
            DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            terminated = false;
            keepAliveListener(socketThread, socket);
            readerMessageListener(socketThread, reader);
            writerMessageListener(socketThread, writer, socket);
            log.info("Connection established: " + socket);
            socketThread.getSocketMessage().onConnectSuccess(socketThread, socket);
        } catch (IOException e) {
            log.severe(e.getMessage() + ", Automatic reconnection...");
            if (socketThread.getSocketMessage().onConnectFailed(e)) {
                disconnect(socket);
                connection(host, port);
            }
        }
    }

    /**
     * 读取消息侦听器
     *
     * @param socketThread 套接字李连接池
     * @param reader       读取数据流
     * @return
     */
    private void readerMessageListener(SocketThread socketThread, DataInputStream reader) {
        socketThread.run(() -> {
            try {
                socketThread.getSocketMessage().onReadMessage(reader);
            } catch (IOException e) {
                socketThread.getSocketMessage().onReadMessageError(e);
            }
        });
    }

    /**
     * 写入消息侦听器
     *
     * @param socketThread 套接字李连接池
     * @param writer       写入数据流
     * @param socket       套接字连接对象
     * @return
     */
    private void writerMessageListener(SocketThread socketThread, DataOutputStream writer, Socket socket) {
        socketThread.run(() -> {
            while (!terminated) {
                socketThread.getSocketMessage().onSendMessageBefore(getMessages(), terminated);
                if (!getMessages().isEmpty()) {
                    try {
                        byte[] bytes = getMessages().poll();
                        socketThread.getSocketMessage().onSendMessage(writer, bytes);
                    } catch (IOException e) {
                        if (socketThread.getSocketMessage().onSendMessageError(e, getMessages())) {
                            socketThread.stop();
                            terminated = true;
                            break;
                        }
                    }
                }
            }
            disconnect(socket);
            connection(host, port);
        });
    }

    /**
     * 连接保持倾听器
     *
     * @param socketThread 套接字李连接池
     * @param socket       套接字连接对象
     * @return
     */
    private void keepAliveListener(SocketThread socketThread, Socket socket) {
        socketThread.run(() -> {
            while (true) {
                long millis = socketThread.getSocketMessage().heartRate();
                if (millis > 0 && !getMessages().isEmpty()) {
                    SocketThread.sleep(millis);
                    try {
                        socket.sendUrgentData(socketThread.getSocketMessage().heartRateData());
                    } catch (IOException e) {
                        log.severe(e.getMessage() + ", " + millis + "ms after, Automatic reconnection...");
                        socketThread.getSocketMessage().heartRateDataError(e);
                        socketThread.stop();
                        terminated = true;
                        break;
                    }
                }
            }
        });
    }
}
