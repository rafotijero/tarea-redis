package com.rafo.apps.task_openfeign.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String objectToString(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String errorMessage = String.format(
                    Constants.ERROR_CONVERT_TO_STRING,
                    object.getClass().getName(),
                    e.getMessage()
            );
            throw new RuntimeException(errorMessage, e);
        }
    }

    public static <T> T stringToObject(String json, Class<T> typeClass) {
        try {
            return OBJECT_MAPPER.readValue(json, typeClass);
        } catch (JsonProcessingException e) {
            String errorMessage = String.format(
                    Constants.ERROR_CONVERT_FROM_STRING,
                    typeClass.getName(),
                    e.getMessage()
            );
            throw new RuntimeException(errorMessage, e);
        }
    }

}
