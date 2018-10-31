package com.tramonti.weather.validator;

import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.repository.UserRepository;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

@Component
public class UserValidatorImpl implements UserValidator {

    @Autowired
    private Validator beanValidator;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void validateBean(User user) {
        Set<ConstraintViolation<User>> violations = beanValidator.validate(user);
        if (violations.size() != 0) {
            StringBuilder descriptionBuilder = new StringBuilder();
            for (ConstraintViolation<User> v : violations) {
                descriptionBuilder.append(v.getMessage() + " ");
            }
            throw new UserException()
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setName("business rules violation")
                    .setDescription(descriptionBuilder.toString())
                    .setClassName(this.getClass().getName());
        }
    }

    @Override
    public void validateFieldsNotEmpty(User user, String... fields) {
        Set<String> violations = getViolations(user, fields);

        if (violations.size() != 0) {
            StringJoiner descriptionJoiner = new StringJoiner(" | ", "[", "]");
            for (String v : violations) {
                descriptionJoiner.add(v);
            }
            throw new UserException()
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setName("business rules violation")
                    .setDescription(descriptionJoiner.toString())
                    .setClassName(this.getClass().getName());
        }
    }

    private Set<String> getViolations(User user, String[] fields) {
        Set<String> violations = new TreeSet<>();

        for (String fieldName : fields) {
            Field field = getField(fieldName);
            if (isNotEmptyAnnotated(field) && isStringType(field)) {
                String fieldString = (String) getFiledValue(user, field);
                if (fieldString == null || fieldString.length() == 0) {
                    String message = getAnnotationMessage(field);
                    violations.add(message);
                }
            }
        }
        return violations;
    }

    private Field getField(String fieldName) {
        try {
            Class<?> clazz = User.class;
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            NDC.push("[exception = " + e.toString()+"]");
            throw new UserException()
                    .setLevel(UserException.Level.ERROR)
                    .setStatus(HttpStatus.CONFLICT)
                    .setName("validation failed")
                    .setDescription("it is not possible to validate user.fields")
                    .setClassName(this.getClass().getName());
        }
    }

    private boolean isNotEmptyAnnotated(Field field) {
        return field.isAnnotationPresent(NotEmpty.class);
    }

    private boolean isStringType(Field field) {
        return field.getType() == String.class;
    }

    private Object getFiledValue(User user, Field field) {
        Object result;
        try {
            field.setAccessible(true);
            result = field.get(user);
        } catch (IllegalAccessException e) {
            NDC.push("[exception = " + e.toString()+"]");
            throw new UserException()
                    .setLevel(UserException.Level.ERROR)
                    .setStatus(HttpStatus.CONFLICT)
                    .setName("validation failed")
                    .setDescription("it is not possible to validate user.fields")
                    .setClassName(this.getClass().getName());
        }
        return result;
    }

    private String getAnnotationMessage(Field field) {
        NotEmpty notEmptyAnnotation = field.getAnnotation(NotEmpty.class);
        return notEmptyAnnotation.message();
    }

    @Override
    public void validateUserIdExists(User user) {
        User foundUser = userRepository.find(user);
        if (foundUser == null) {
            throw new UserException()
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setName("user does not exist")
                    .setDescription("there is no user with id " + user.getId())
                    .setClassName(this.getClass().getName());
        }
    }

    @Override
    public void validateUserMatchesExistingUser(User user) {
        User foundUser = userRepository.find(user);
        if (!user.equals(foundUser)) {
            throw new UserException()
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setName("user does not exist")
                    .setDescription("entered user does not match existing user")
                    .setClassName(this.getClass().getName());
        }
    }

    @Override
    public void validateUserNameExists(User user) {
        User foundUser = userRepository.findUserName(user);
        if (foundUser != null) {
            throw new UserException()
                    .setLevel(UserException.Level.WARNING)
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setName("user already exists")
                    .setDescription("user with such username already exists")
                    .setClassName(this.getClass().getName());
        }
    }

}
