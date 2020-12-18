package com.cmsen.common.util;

import com.cmsen.common.lang.JsonArray;
import com.cmsen.common.lang.JsonObject;
import junit.framework.TestCase;

public class JsonUtilTest extends TestCase {
    String jsonObjectStr = "{\"id\":1,\"name\":\"JsonUtil\",\"nullAttr\":null}";
    String jsonArrayStr = "[{\"id\":1,\"name\":\"JsonUtil\",\"nullAttr\":null}]";

    public void test() {
        testData testData = JsonUtil.toClass(jsonObjectStr, testData.class);
        JsonObject jsonObject = JsonUtil.toClass(jsonObjectStr, JsonObject.class);
        JsonArray jsonArray = JsonUtil.toClass(jsonArrayStr, JsonArray.class);
        assertTrue(testData.getId() == 1);
        assertEquals(jsonObject.get("id"), 1);
    }

    private static class testData {
        private int id;
        private String name;
        private String nullAttr;

        public testData() {
        }

        public testData(int id, String name, String nullAttr) {
            this.id = id;
            this.name = name;
            this.nullAttr = nullAttr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNullAttr() {
            return nullAttr;
        }

        public void setNullAttr(String nullAttr) {
            this.nullAttr = nullAttr;
        }

        @Override
        public String toString() {
            return testData.class.getSimpleName() + "{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", nullAttr='" + nullAttr + '\'' +
                    '}';
        }
    }

}