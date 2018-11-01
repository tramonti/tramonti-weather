package com.tramonti.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;

import java.io.IOException;

public class StubFactory {
    private static final String RESOURCE_PATTERN = "stubs/{CLASS_DIR}/{FILE}.json";

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.findAndRegisterModules();
    }

    public static <T> T getStub(String resourceFileName, Class clazz) {
        String jsonUrl = formURL(resourceFileName, clazz);
        Object result;
        try {
            result = mapper.readValue(Resources.getResource(jsonUrl).openStream(), clazz);
        } catch (IOException e) {
            throw new TestException(e);
        }
        return (T) result;
    }

    private static String formURL(String resourceFileName, Class clazz) {
        String className = clazz.getSimpleName();
        if (clazz.isArray()) {
            className = className.replace("[]", "");
        }
        String resourceUrl = RESOURCE_PATTERN
                .replace("{CLASS_DIR}", className)
                .replace("{FILE}", resourceFileName);
        return resourceUrl;
    }

    public static <T> String formJsonString(T object){
        try {
            return mapper.writeValueAsString(object);
        }catch (JsonProcessingException e) {
            throw new TestException(e);
        }
    }
}
