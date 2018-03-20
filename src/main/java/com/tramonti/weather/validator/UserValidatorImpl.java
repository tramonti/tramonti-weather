package com.tramonti.weather.validator;

import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UserValidatorImpl implements UserValidator {

    @Override
    public void validateId(User user) {
        if (user.getId() == null || user.getId().length() == 0) {
            throw new UserException()
                    .setDescription("user id cannot be empty")
                    .setName("Null pointer")
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setThrowable(new NullPointerException());
        }
    }

    @Override
    public void validateAllUser(User user, User existingUser) {
        if (!user.equals(existingUser)) {
            throw new UserException()
                    .setDescription("User with such parameters does not exist")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setLevel(UserException.Level.WARNING)
                    .setName("Validation rules")
                    .setThrowable(new IllegalAccessError());
        }
    }
}
