package com.cmsen.common.util;

import junit.framework.TestCase;

public class FileMimeTest extends TestCase {
    public void test() {
        assertEquals(FileMime.get("a/image/test.jpg"), FileMime.get(FileMime.JPG));
        assertEquals(FileMime.get("Not recognized file extension type", ""), "");
        assertEquals(FileMime.get("Non MIME or null string", FileMime.PNG), FileMime.PNG.getMimeType());
        assertEquals(FileMime.get(FileMime.PNG), FileMime.PNG.getMimeType());
        assertTrue(FileMime.has("a/image/test.jpg"));
        assertTrue(FileMime.has("jpg"));
        assertTrue(FileMime.has(".jpg"));
        assertTrue(FileMime.has("JPG"));
        assertFalse(FileMime.has(""));
        assertFalse(FileMime.has(".errExtension"));
    }
}