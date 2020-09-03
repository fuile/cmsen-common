/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.socket;

import java.net.InetAddress;

public abstract class SocketConstant {
    public static final String HOST_ADDRESS = InetAddress.getLoopbackAddress().getHostAddress();
    public static final int HOST_PORT = 5209;
}
