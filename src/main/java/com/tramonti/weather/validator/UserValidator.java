package com.tramonti.weather.validator;

import com.tramonti.weather.domain.User;

public interface UserValidator {
    void validateId(User user);

    void validateAllUser(User user, User existingUser);
}
