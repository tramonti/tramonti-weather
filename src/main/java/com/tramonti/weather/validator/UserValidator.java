package com.tramonti.weather.validator;

import com.tramonti.weather.domain.user.User;

//TODO: use spring validators
public interface UserValidator {
    void validateId(User user);

    void validateAllUser(User user, User existingUser);
}
