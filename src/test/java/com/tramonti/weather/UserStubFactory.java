package com.tramonti.weather;

import com.tramonti.weather.domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class UserStubFactory {

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
}
