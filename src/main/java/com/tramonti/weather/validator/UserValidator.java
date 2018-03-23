package com.tramonti.weather.validator;

import com.tramonti.weather.domain.user.User;

public interface UserValidator {

    void validateBean(User user);

    void validateFieldsNotEmpty(User user, String... fields);

    void validateUserIdExists(User user);

    void validateUserMatchesExistingUser(User user);

    void validateUserNameExists(User user);
}
