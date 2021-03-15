package com.cmsen.common.util;

import junit.framework.TestCase;

public class AppUtilTest extends TestCase {
    public void test() {
        String secret = AppUtil.secret("secret string test");
        String id = AppUtil.id(secret);
        assertEquals(id, "924606434");
        assertEquals(secret, "66a22150b5b613469202a561db8eb63bc9d1c0e1");
    }
}