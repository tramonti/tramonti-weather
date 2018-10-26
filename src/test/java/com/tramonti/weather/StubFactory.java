package com.tramonti.weather;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.tramonti.weather.domain.user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StubFactory {
    private static final String RESOURCE_PATTERN = "stubs/{CLASS_DIR}/{FILE}.json";


    public static List<User> getStubUsers() {
        ArrayList<User> users = new ArrayList<>();
        User u1 = new User();
        u1.setId("5aa64f12f3536b2eacb883d");
        u1.setPassword("haslfdhaf");
        u1.setUsername("qwe");

        User u2 = new User();
        u2.setId("63df2fds8963khkcs232d32eds2d");
        u2.setPassword("dasfhlshf");
        u2.setUsername("zxcvb");
        users.add(u1);
        users.add(u2);
        return users;
    }

    public static <T> T getStub(String resourceFileName, Class clazz) {
        String jsonUrl = formURL(resourceFileName, clazz);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Object result;
        try {
            result = mapper.readValue(Resources.getResource(jsonUrl).openStream(), clazz);
        } catch (IOException e) {
            throw new TestException(e);
        }
        return (T) result;
    }

    private static String formURL(String resourceFileName, Class clazz) {
        String resourceUrl = RESOURCE_PATTERN
                .replace("{CLASS_DIR}", clazz.getSimpleName())
                .replace("{FILE}", resourceFileName);
        return resourceUrl;
    }
}
