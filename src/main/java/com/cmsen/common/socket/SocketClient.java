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
    private boolean terminated;

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

    public void disconnect(Socket socket) {
        if (null != socket && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
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
            socketThread.getSocketMessage().onConnectSuccess(socketThread, socket);
        } catch (IOException e) {
            if (socketThread.getSocketMessage().onConnectFailed(e)) {
                disconnect(socket);
                connection(host, port);
            }
        }
    }

    private void readerMessageListener(SocketThread socketThread, DataInputStream reader) {
        socketThread.run(() -> {
            try {
                socketThread.getSocketMessage().onReadMessage(reader);
            } catch (IOException e) {
                socketThread.getSocketMessage().onReadMessageError(e);
            }
        });
    }


    private void writerMessageListener(SocketThread socketThread, DataOutputStream writer, Socket socket) {
        socketThread.run(() -> {
            while (!terminated) {
                if (message.size() > 0) {
                    try {
                        socketThread.getSocketMessage().onSendMessage(writer, message.get(0));
                        message.remove(0);
                    } catch (IOException e) {
                        socketThread.getSocketMessage().onSendMessageError(e, message);
                    }
                }
            }
            disconnect(socket);
            connection(host, port);
        });
    }

    private void keepAliveListener(SocketThread socketThread, Socket socket) {
        socketThread.run(() -> {
            long millis = socketThread.getSocketMessage().heartRate();
            while (true) {
                if (millis > 0) {
                    try {
                        socket.sendUrgentData(socketThread.getSocketMessage().heartRateData());
                    } catch (IOException e) {
                        socketThread.getSocketMessage().heartRateDataError(e);
                        socketThread.stop();
                        terminated = true;
                        break;
                    }
                    SocketThread.sleep(millis);
                }
            }
        });
    }
}
