package com.cmsen.common.util;

import junit.framework.TestCase;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;


public class StringUtilTest extends TestCase {
    public void test() {
        String str = "";
        System.err.println("str=" + str);
        System.err.println("isBlank: " + StringUtil.isBlank(str));
        System.err.println("isNotBlank: " + StringUtil.isNotBlank(str));
        System.err.println("isNotEmpty: " + StringUtil.isNotEmpty(str));
        System.err.println("isEmpty: " + StringUtil.isEmpty(str));
        System.err.println("isEmptyChar: " + StringUtil.isEmptyChar(str));


        final String[] sses = transformPath("/df\\aa/upload", UUID.randomUUID().toString() + ".png");
        System.err.println(Arrays.toString(sses));

    }

    public static String[] transformPath(String path, String filename) {
        String regex = "([\\|/]+)";
        String separator = File.separator;
        path = path.replaceAll(regex, separator.replace("\\", "\\\\"));
        // 判断是否已分割符结尾，如果没有测自动补充
        if (!path.endsWith(separator)) {
            path = path + separator;
        }
        System.err.println(path);
        System.err.println(path);
        return new String[]{path + filename, path, filename};
    }
}