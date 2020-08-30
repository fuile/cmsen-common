/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SocketClient {
    private String host;
    private int port;
    private int timeOut;
    private List<byte[]> message = new LinkedList<>();
    private SocketMessage socketMessage;
    private boolean connect;
    private boolean keepAlive;

    public SocketClient() {
    }

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public SocketClient(String host, int port, int timeOut) {
        this(host, port);
        this.timeOut = timeOut;
    }

    public SocketClient(String host, int port, boolean keepAlive) {
        this(host, port);
        this.keepAlive = keepAlive;
    }

    public SocketClient(String host, int port, List<byte[]> message) {
        this(host, port);
        this.message = message;
    }

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

    public List<byte[]> getSendMessages() {
        return message;
    }

    public SocketClient sendMessage(byte[] message) {
        this.message.add(message);
        return this;
    }

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

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

    public void connection() {
        connection(host, port);
    }

    public void connection(SocketMessage socketMessage) {
        connection(host, port, socketMessage);
    }

    public void connection(String host, int port) {
        connection(host, port, socketMessage);
    }

    public void connection(String host, int port, SocketMessage socketMessage) {
        connect = true;
        if (null == host) {
            throw new IllegalArgumentException("connect: Address is invalid on local machine");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("connect: port is not valid on remote machine");
        }
        try {
            Socket socket = new Socket(host, port);
            socket.setKeepAlive(keepAlive);
            socket.setSoTimeout(timeOut);
            DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            keepAliveListener(socket, socketMessage);
            readerMessageListener(reader, socketMessage);
            writerMessageListener(writer, socketMessage);
            System.out.println("Connection established: " + socket);
        } catch (IOException e) {
            System.err.println(String.format("%s, %s", e.getMessage(), "Automatic reconnection..."));
            connection(host, port);
        }
    }

    private void readerMessageListener(DataInputStream reader, SocketMessage socketMessage) {
        SocketThread.create(() -> {
            try {
                socketMessage.onReadMessage(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void writerMessageListener(DataOutputStream writer, SocketMessage socketMessage) {
        SocketThread.create(() -> {
            while (true) {
                if (message.size() > 0) {
                    try {
                        byte[] bytes = message.get(0);
                        socketMessage.onSendMessage(writer, bytes);
                        message.remove(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void keepAliveListener(Socket socket, SocketMessage socketMessage) {
        SocketThread.create(() -> {
            long millis = socketMessage.heartRate();
            int data = socketMessage.heartRateData();
            while (true) {
                if (millis > 0) {
                    SocketThread.sleep(millis);
                    try {
                        socket.sendUrgentData(data);
                    } catch (IOException e) {
                        System.err.println(String.format("%s, %dms after, %s", e.getMessage(), millis, "Automatic reconnection..."));
                        connection(host, port);
                        break;
                    }
                }
            }
        });
    }
}
