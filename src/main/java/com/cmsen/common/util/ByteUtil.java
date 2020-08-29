/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

public class ByteUtil {
    public static byte[] merger(byte[]... values) {
        int byteLength = 0;
        for (int i = 0; i < values.length; i++) {
            byteLength += values[i].length;
        }
        byte[] bytes = new byte[byteLength];
        int currentByteLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] currentByte = values[i];
            System.arraycopy(currentByte, 0, bytes, currentByteLength, currentByte.length);
            currentByteLength += currentByte.length;
        }
        return bytes;
    }

    public static int toInt(byte b[]) {
        return b[3] & 0xff
                | (b[2] & 0xff) << 8
                | (b[1] & 0xff) << 16
                | (b[0] & 0xff) << 24;
    }

    public static byte[] toBytes(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    public static int toInt2(byte b[]) {
        return b[1] & 0xff | (b[0] & 0xff) << 8;
    }

    public static byte[] toBytes2(int n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    public static String toHex(byte[] buf) {
        return toHex(buf, " ");
    }

    public static String toHex(byte[] buf, String placeholder) {
        StringBuffer strBuf = new StringBuffer();
        for (byte b : buf) {
            if (b == 0) {
                strBuf.append("00");
            } else if (b == -1) {
                strBuf.append("FF");
            } else {
                String str = Integer.toHexString(b).toUpperCase();
                if (str.length() == 8) {
                    str = str.substring(6, 8);
                } else if (str.length() < 2) {
                    str = "0" + str;
                }
                strBuf.append(str);
            }
            if (null != placeholder) {
                strBuf.append(placeholder);
            }
        }
        return strBuf.toString();
    }

    /**
     * Byte数组转十六进制字符串
     *
     * @param bytes Byte数组
     * @return 十六进制字符串
     */
    public static String toHex16String(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        if (bytes != null) {
            for (Byte b : bytes) {
                hex.append(String.format("%02X", b.intValue() & 0xFF));
            }
        }
        return hex.toString();
    }
}
