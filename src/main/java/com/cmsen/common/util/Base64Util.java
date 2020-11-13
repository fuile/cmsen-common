/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Base64
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class Base64Util {
    public static byte[] encode(byte[] src) {
        return Base64.getEncoder().encode(src);
    }

    public static String encodeToString(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    public static byte[] decode(byte[] src) {
        return Base64.getDecoder().decode(src);
    }

    public static byte[] decode(String src) {
        return Base64.getDecoder().decode(src);
    }

    public static boolean toSaveFile(String filename, String base64) {
        byte[] b = decode(base64);
        for (int i = 0; i < b.length; i++) {
            if (b[0] < 0) {
                b[0] += 256;
            }
        }
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(b);
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String fileToBase64(String filename) {
        return fileToBase64(new File(filename));
    }

    public static String fileToBase64(File file) {
        try (FileInputStream fin = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fin.read(data);
            return encodeToString(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
