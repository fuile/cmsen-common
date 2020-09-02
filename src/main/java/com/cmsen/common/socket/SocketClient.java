/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import com.cmsen.common.util.ByteUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SocketClient {
    private String serverIp;
    private int serverPort;
    private int sendBufferSize;
    private int receiveBufferSize;
    private Socket socket;
    private SocketThread socketThread;
    private SocketChannel socketChannel;
    private SocketMessage socketMessage;
    private BlockingQueue<byte[]> messageQueue;
    private boolean blockingMode;
    private boolean keepAlive;
    private int soTimeout;
    private int resetWaitTime;
    private final Logger log = Logger.getLogger(SocketClient.class.getName());

    public SocketClient() {
        this("127.0.0.1", 5209);
    }

    public SocketClient(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.sendBufferSize = 10;
        this.receiveBufferSize = 8;
        this.resetWaitTime = 3000;
        this.messageQueue = new ArrayBlockingQueue<>(100);
    }

    public SocketClient(String serverIp, int serverPort, SocketMessage socketMessage) {
        this(serverIp, serverPort);
        this.socketMessage = socketMessage;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public void setMessageQueueSize(int messageQueueSize) {
        this.messageQueue = new ArrayBlockingQueue<>(messageQueueSize);
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

    public void setSocketMessage(SocketMessage socketMessage) {
        this.socketMessage = socketMessage;
    }

    public void setSocketMessage(Class<? extends SocketMessage> socketMessage) {
        try {
            this.socketMessage = socketMessage.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.severe("setSocketMessage object fail: " + e.getMessage());
        }
    }

    public byte[] getMessage(boolean isDelete) {
        return !isDelete ? messageQueue.peek() : messageQueue.poll();
    }

    public Message setMessage(byte[] data) {
        if (!messageQueue.offer(data)) {
            log.severe("setMessage fail: using a capacity-restricted");
        }
        return new Message(this);
    }

    public Message setMessage(int... data) {
        if (!messageQueue.offer(ByteUtil.toBytes2(data))) {
            log.severe("setMessage fail: using a capacity-restricted");
        }
        return new Message(this);
    }

    public Message setMessage(byte[]... data) {
        if (!messageQueue.offer(ByteUtil.merger(data))) {
            log.severe("setMessage fail: " + Arrays.toString(data));
        }
        return new Message(this);
    }

    public Message setMessage(String data) {
        if (!messageQueue.offer(data.getBytes())) {
            log.severe("setMessage fail: using a capacity-restricted");
        }
        return new Message(this);
    }

    public BlockingQueue<byte[]> getMessageQueue() {
        return messageQueue;
    }

    public void setBlockingMode(boolean blockingMode) {
        this.blockingMode = blockingMode;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setResetWaitTime(int milliseconds) {
        if (resetWaitTime < 0) {
            throw new IllegalArgumentException("Reset wait time can't be negative");
        }
        this.resetWaitTime = milliseconds;
    }

    public void send() {
        if (!messageQueue.isEmpty()) {
            ByteBuffer buffer = ByteBuffer.allocate(sendBufferSize);
            buffer.clear();
            buffer.put(getMessage(true));
            buffer.flip();
            while (buffer.hasRemaining()) {
                try {
                    socketMessage.send(buffer);
                    socketChannel.write(buffer);
                } catch (Exception e) {
                    if (socketMessage.sendError(e)) {
                        reset();
                    }
                }
            }
        }
    }

    private void receive() {
        ByteBuffer buffer = ByteBuffer.allocate(receiveBufferSize);
        try {
            while (socketChannel.read(buffer) != -1) {
                buffer.flip();
                if (buffer.hasRemaining() && buffer.limit() == receiveBufferSize) {
                    socketMessage.receive(buffer);
                }
                buffer.clear();
            }
        } catch (Exception e) {
            if (socketMessage.receiveError(e)) {
                receive();
            }
        }
    }

    private void heartRate() {
        while (true) {
            long millis = socketMessage.heartRate();
            if (millis > 0 && messageQueue.isEmpty()) {
                try {
                    SocketThread.sleep(millis);
                    socket.sendUrgentData(socketMessage.heartRateData());
                } catch (Exception e) {
                    if (socketMessage.heartRateError(e)) {
                        reset();
                        break;
                    }
                }
            }
        }
    }

    public void open() {
        open(serverIp, serverPort);
    }

    public void open(String serverIp, int serverPort) {
        socketThread = new SocketThread(socketMessage.heartRate() > 0 ? 2 : 1);
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(blockingMode);
            socketChannel.connect(new InetSocketAddress(serverIp, serverPort));
            if (!socketChannel.finishConnect()) {
                throw new IOException("connection is interrupted: " + serverIp + ":" + serverPort);
            }
            socket = socketChannel.socket();
            socket.setKeepAlive(keepAlive);
            socket.setSoTimeout(soTimeout);

            log.info(String.format("Connection established: %s:%s, client: %s:%s",
                    socket.getInetAddress(),
                    socket.getPort(),
                    socket.getLocalAddress(),
                    socket.getLocalPort()
            ));

            if (socketMessage.heartRate() > 0) {
                socketThread.run(this::heartRate);
            }

            socketThread.run(this::receive);
            socketMessage.finishConnect(socket);
        } catch (Exception e) {
            log.severe("Connect error: " + e.getMessage());
            if (socketMessage.connectError(e)) {
                reset();
            }
        }
    }

    public void close() {
        try {
            if (null != socket && !socket.isClosed()) {
                socket.close();
            }
            if (null != socketChannel && socketChannel.isOpen()) {
                socketChannel.close();
            }
            if (null != socketThread) {
                socketThread.stop();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        if (resetWaitTime > 0) {
            try {
                long sec = TimeUnit.MILLISECONDS.toSeconds(resetWaitTime);
                log.severe("Error happened when establishing connection, try again " + sec + "s later");
                TimeUnit.MILLISECONDS.sleep(resetWaitTime);
                close();
                open();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
