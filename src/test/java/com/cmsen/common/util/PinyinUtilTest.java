package com.cmsen.common.util;

import junit.framework.TestCase;

public class PinyinUtilTest extends TestCase {
    public void test() {
        String str = "你#好";
        assertEquals(PinyinUtil.allPy(str), "ni_hao");
        assertEquals(PinyinUtil.firstPy(str), "n_h");
    }
}