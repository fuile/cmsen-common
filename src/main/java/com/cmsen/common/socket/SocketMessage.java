/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * 套接字消息
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public interface SocketMessage {
    /**
     * 心跳检测间隔
     *
     * @return long [0=关闭心跳检测]
     */
    default long heartRate() {
        return 0;
    }

    /**
     * 心跳检测数据
     *
     * @return int
     */
    default int heartRateData() {
        return 0xff;
    }

    /**
     * 心跳检测错误
     *
     * @param e 异常信息
     * @return boolean [true=连接失败自动重连], 默认false关闭自动重连
     */
    default boolean heartRateError(Exception e) {
        return false;
    }


    /**
     * 完成连接时
     *
     * @param socket 套接字对象
     */
    default void finishConnect(Socket socket) {
    }

    /**
     * 连接错误
     *
     * @param e 异常信息
     * @return boolean [true=连接失败自动重连], 默认false关闭自动重连
     */
    default boolean connectError(Exception e) {
        return false;
    }

    /**
     * 消息发送时
     *
     * @param buffer 正在发送的消息
     * @throws Exception
     */
    default void send(ByteBuffer buffer) throws Exception {
    }

    /**
     * 消息发送时错误打印
     *
     * @param e 异常信息
     * @return boolean [true=发送失败自动重连], 默认false关闭自动重连
     */
    default boolean sendError(Exception e) {
        return false;
    }

    /**
     * 消息接收时
     *
     * @param buffer 正在接收的消息
     * @throws Exception
     */
    void receive(ByteBuffer buffer) throws Exception;

    /**
     * 消息接收时错误打印
     *
     * @param e 异常信息
     * @return boolean [true=尝试重新接收]
     */
    default boolean receiveError(Exception e) {
        return false;
    }
}
