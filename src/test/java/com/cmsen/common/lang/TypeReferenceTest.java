package com.cmsen.common.lang;

import junit.framework.TestCase;

public class TypeReferenceTest extends TestCase {
    public void test() {
        System.err.println("------- 用例一 -------");
        GetType<TestClass> testClassGetType = new GetType<TestClass>() {
        };
        System.err.println("获取类型名称：" + testClassGetType.getType().getTypeName());
        System.err.println("实例化：" + testClassGetType.newInstanceType());

        System.err.println("------- 用例二 -------");
        System.err.println("获取类型名称：" + (new GetType<TestClass>() {
        }).getType().getTypeName());
        System.err.println("实例化：" + (new GetType<TestClass>() {
        }).newInstanceType());
    }

    public static class GetType<T> extends TypeReference<T> {
    }

    public static class TestClass {
        private String field = "success";

        @Override
        public String toString() {
            return "TestClass{" +
                    "field='" + field + '\'' +
                    '}';
        }
    }
}