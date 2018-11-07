package com.tramonti.weather.service;

import com.tramonti.weather.domain.user.User;

import java.util.List;

/**
 * Created by tarashrynchuk on 3/1/18.
 */

public interface UserService {
    List<User> findAll();

    User find(String id);

    User create(User user);

    User update(User user);

    User delete(User user);
}
