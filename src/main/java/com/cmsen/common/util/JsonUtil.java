package com.cmsen.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 *
 * @author jared.Yan (yanhuaiwen@163.com)
 */
@Deprecated
public class JsonUtil {
    public static Map<?, ?> toObject(String string) {
        return toClass(string, Map.class);
    }

    public static List<?> toArray(String string) {
        return toClass(string, List.class);
    }

    public static <T> T toClass(String string, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(string, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String toString(Object value, boolean nonNull) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (nonNull) {
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            }
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
