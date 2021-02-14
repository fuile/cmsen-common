/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public abstract class ReflectionUtil {
    public static Object invokeMethodGet(Class<?> clazz, String name) {
        AssertUtil.notNull(clazz, "Class must not be null");
        try {
            Class<?> superclass = clazz.getSuperclass();
            Method method = superclass.getMethod("get".concat(name.substring(0, 1).toUpperCase() + name.substring(1)));
            return method.invoke(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethodSet(Class<?> clazz, String name, Object... args) {
        AssertUtil.notNull(clazz, "Class must not be null");
        try {
            Class<?> superclass = clazz.getSuperclass();
            Field field = superclass.getDeclaredField(name);
            Method method = superclass.getMethod("set".concat(name.substring(0, 1).toUpperCase() + name.substring(1)), field.getType());
            return method.invoke(clazz, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Modifiers findFields(Class<?> clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        Class<?> superclass = clazz.getSuperclass();
        Field[] declaredFields = superclass.getDeclaredFields();
        Modifiers fieldMap = new Modifiers();
        for (Field declaredField : declaredFields) {
            if (Modifier.isPrivate(declaredField.getModifiers())) {
                fieldMap.setPrivateFields(declaredField.getName());
            } else if (Modifier.isProtected(declaredField.getModifiers())) {
                fieldMap.setProtectedFields(declaredField.getName());
            } else if (Modifier.isPublic(declaredField.getModifiers())) {
                fieldMap.setPublicFields(declaredField.getName());
            }
        }
        return fieldMap;
    }

    public static class Modifiers {
        private List<String> privateFields = new LinkedList<>();
        private List<String> protectedFields = new LinkedList<>();
        private List<String> publicFields = new LinkedList<>();

        public List<String> getPrivateFields() {
            return privateFields;
        }

        public void setPrivateFields(List<String> privateFields) {
            this.privateFields = privateFields;
        }

        public void setPrivateFields(String privateFields) {
            this.privateFields.add(privateFields);
        }

        public List<String> getProtectedFields() {
            return protectedFields;
        }

        public void setProtectedFields(List<String> protectedFields) {
            this.protectedFields = protectedFields;
        }

        public void setProtectedFields(String protectedFields) {
            this.protectedFields.add(protectedFields);
        }

        public List<String> getPublicFields() {
            return publicFields;
        }

        public void setPublicFields(List<String> publicFields) {
            this.publicFields = publicFields;
        }

        public void setPublicFields(String publicFields) {
            this.publicFields.add(publicFields);
        }
    }
}
