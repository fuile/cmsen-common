/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PinyinUtil {
    private static Map<String, String> pys;

    public static String allPy(String str) {
        return transform(str, "_", false);
    }

    public static String firstPy(String str) {
        return transform(str, "_", true);
    }

    public static String transform(String str, String placeholder, boolean firstPy) {
        if (pys == null) {
            String data = FileUtil.getResource("dict.dat");
            String[] rows = data.split("\\|");
            pys = new HashMap<>();
            for (String row : rows) {
                String[] py = row.split(":");
                if (py.length > 0) {
                    String[] chars = py[1].split(",");
                    for (String aChar : chars) {
                        pys.put(aChar, py[0]);
                    }
                }
            }
        }
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            String chr = str.substring(i, i + 1);
            String s = pys.get(chr);
            int ord = ord(chr);
            if (ord < 0x80) {
                if ((ord >= 48 && ord <= 57) || (ord >= 97 && ord <= 122)) {
                    rs.append(chr);
                } else {
                    rs.append(ord >= 65 && ord <= 90 ? chr.toLowerCase() : placeholder);
                }
            } else {
                rs.append(s != null ? (firstPy ? s.substring(0, 1) : s) : placeholder);
            }
        }
        return rs.toString();
    }

    //& 0xff 针对utf-8编码
    public static int ord(String s) {
        return s.length() > 0 ? (s.getBytes(StandardCharsets.UTF_8)[0] & 0xff) : 0;
    }

    public static int ord(char c) {
        return c < 0x80 ? c : ord(Character.toString(c));
    }

    public static String chr(char i) {
        return Character.toString((char) i);
    }

    public static void reset() {
        pys = null;
    }
}
