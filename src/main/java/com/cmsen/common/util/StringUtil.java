package com.cmsen.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Formatter;
import java.util.Random;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 字符串工具类
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class StringUtil {
    public static boolean isBlank(CharSequence cs) {
        int charLen = length(cs);
        if (charLen != 0) {
            for (int i = 0; i < charLen; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isNotEmptyChar(CharSequence cs) {
        return !isEmptyChar(cs);
    }

    public static boolean isEmptyChar(CharSequence cs) {
        return cs != null && cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();
            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Compares whet her two lists are equal
     *
     * @param list1 the compares list1
     * @param list2 the compares list2
     * @return boolean
     */
    public static boolean isListEquals(Collection<?> list1, Collection<?> list2) {
        return list1.containsAll(list2) && list2.containsAll(list1);
    }

    public static String firstUpperCase(String letter) {
        if (letter != null) {
            return letter.substring(0, 1).toUpperCase() + letter.substring(1);
        }
        return null;
    }

    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String reverse(String str) {
        return str == null ? null : (new StringBuilder(str)).reverse().toString();
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String UUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) +
                uuid.substring(9, 13) +
                uuid.substring(14, 18) +
                uuid.substring(19, 23) +
                uuid.substring(24);
    }

    public static int randomNumber(int len) {
        return new Random().nextInt(len) + 1;
    }

    /**
     * 字节转十六进制
     *
     * @param bytes 字节
     * @return HEX
     */
    public static String byteToHex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 十六进制透明
     *
     * @param alpha 透明度 0.0~1.0
     * @return HEX
     */
    public static String transparencyToHex(double alpha) {
        if (alpha > 1 || alpha < 0) {
            throw new IllegalArgumentException("The alpha parameter exceeds the range of 0.0 to 1.0");
        }
        double i = Math.round(alpha * 100) / 100.0d;
        String hex = Integer.toHexString((int) Math.round(i * 255)).toUpperCase();
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        return hex;
    }

    public static String compress(byte[] str) throws IOException {
        if (str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str);
        gzip.close();
        return out.toString(StandardCharsets.ISO_8859_1.toString());
    }

    public static String compress(String str) throws IOException {
        if (str == null) {
            return null;
        }
        return compress(str.getBytes());
    }

    public static String unCompress(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(StandardCharsets.ISO_8859_1));
             GZIPInputStream gzip = new GZIPInputStream(in)) {
            int offset = 1024;
            byte[] buffer = new byte[offset];
            while ((offset = gzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符转码
     *
     * @param str 字符
     * @return String
     */
    public static String transcoding(String str) {
        return transcoding(str, getCharsetName(str));
    }

    public static String transcoding(String str, String defCharsetName) {

        return transcoding(str, defCharsetName, Charset.defaultCharset().name());
    }

    public static String transcoding(String str, String defCharsetName, String targetCharsetName) {
        try {
            return new String(str.getBytes(defCharsetName), targetCharsetName);
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * 获取字符集名称
     *
     * @param
     * @return
     */
    public static String getCharsetName(String str) {
        String[] charEncode = {"UTF-8", "GBK", "GB2312", "BIG5", "GB18030", "UTF-16", "UTF-16LE", "UTF-16BE", "UTF-32"};
        String charsetStr = Charset.defaultCharset().name();
        for (String charsetName : charEncode) {
            try {
                if (str.equals(new String(str.getBytes(charsetName), charsetName))) {
                    System.out.println(1);
                    return charsetName;
                }
            } catch (Exception e) {
            }
        }
        for (Charset charset : Charset.availableCharsets().values()) {
            try {
                if (str.equals(new String(str.getBytes(charset.name()), charset.name()))) {
                    System.out.println(2);
                    return charset.name();
                }
            } catch (Exception e) {
            }
        }
        return charsetStr;
    }
}
