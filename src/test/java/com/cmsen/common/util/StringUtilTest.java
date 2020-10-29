package com.cmsen.common.util;

import junit.framework.TestCase;


public class StringUtilTest extends TestCase {
    public void test() {

        assertTrue(StringUtil.isEmpty(null));
        assertTrue(StringUtil.isEmpty(""));
        assertFalse(StringUtil.isEmpty("isEmpty"));

        assertFalse(StringUtil.isNotEmpty(null));
        assertFalse(StringUtil.isNotEmpty(""));
        assertTrue(StringUtil.isNotEmpty("isNotEmpty"));

        assertFalse(StringUtil.isEmptyChar(null));
        assertTrue(StringUtil.isEmptyChar(""));
        assertFalse(StringUtil.isEmptyChar("isEmptyChar"));

        assertTrue(StringUtil.isNotEmptyChar(null));
        assertFalse(StringUtil.isNotEmptyChar(""));
        assertTrue(StringUtil.isNotEmptyChar("isNotEmptyChar"));

        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank(" "));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("isBlank"));

        assertFalse(StringUtil.isNotBlank(null));
        assertFalse(StringUtil.isNotBlank(""));
        assertFalse(StringUtil.isNotBlank(" "));
        assertFalse(StringUtil.isNotBlank("   "));
        assertTrue(StringUtil.isNotBlank("isNotBlank"));
    }
}