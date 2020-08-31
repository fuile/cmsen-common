/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 套接字服务端
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class SocketServer {
    private int port = 5209;
    private static Queue<byte[]> message = new LinkedList<>();
    private SocketMessage socketMessage;
    private boolean start;

    public int getPort() {
        return port;
    }

    public SocketServer setPort(int port) {
        this.port = port;
        return this;
    }

    public Queue<byte[]> getSendMessages() {
        return message;
    }

    public SocketServer sendMessage(byte[] message) {
        this.message.add(message);
        return this;
    }

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

    public SocketServer addMessageListener(SocketMessage socketMessage) {
        if (start) {
            throw new IllegalStateException("addMessageListener must on start() before");
        }
        this.socketMessage = socketMessage;
        return this;
    }

    public boolean isStart() {
        return start;
    }

    public SocketServer() {
    }

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        start(port);
    }

    public void start(int port) {
        start(port, socketMessage);
    }

    public void start(SocketMessage socketMessage) {
        start(port, socketMessage);
    }

    public void start(int port, SocketMessage socketMessage) {
        start = true;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Socket Server started");
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream writer = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                System.out.println("Start listening: " + socket);
                readerMessageListener(reader, socketMessage);
                writerMessageListener(writer, socketMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
                if (!message.isEmpty()) {
                    try {
                        byte[] bytes = message.poll();
                        socketMessage.onSendMessage(writer, bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
