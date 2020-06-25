package com.cmsen.common.util;

import java.util.Collection;
import java.util.UUID;

/**
 * 字符串工具类
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class StringUtil {
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

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int len;
        if (cs != null && (len = cs.length()) != 0) {
            for (int i = 0; i < len; i++) {
                if (Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
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

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String UUID() {
        return UUID.randomUUID()
                .toString()
                .toUpperCase()
                .replaceAll("-", "");
    }
}
