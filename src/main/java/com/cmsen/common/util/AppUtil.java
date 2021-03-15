/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CRC32;

public class AppUtil {
    public static String id(String secret) {
        CRC32 crc32 = new CRC32();
        crc32.update(secret.toUpperCase().getBytes());
        return String.valueOf(crc32.getValue());
    }

    public static String secret(String id) {
        return secret(new String[]{id});
    }

    public static String secret(String... array) {
        return secret("SHA-1", array);
    }

    public static String secret(String algorithm, String... array) {
        try {
            Arrays.sort(array);
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s);
            }
            String str = sb.toString();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexStr = new StringBuilder();
            String shaHex = "";
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean valid(String id, String secret) {
        return id(secret).equals(id);
    }
}
