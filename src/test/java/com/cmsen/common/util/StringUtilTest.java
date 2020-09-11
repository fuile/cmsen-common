package com.cmsen.common.util;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
    public void test() {
        String str = "";

        System.err.println("isBlank: " + StringUtil.isBlank(str));
        System.err.println("isNotBlank: " + StringUtil.isNotBlank(str));
        System.err.println("isNotEmpty: " + StringUtil.isNotEmpty(str));
        System.err.println("isEmpty: " + StringUtil.isEmpty(str));
        System.err.println("isEmptyChar: " + StringUtil.isEmptyChar(str));
    }
}