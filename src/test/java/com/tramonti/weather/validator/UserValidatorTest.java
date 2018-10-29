package com.tramonti.weather.validator;

import com.tramonti.weather.StubFactory;
import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserValidatorTest {

    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator beanValidator;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        userValidator = new UserValidatorImpl();
        ReflectionTestUtils.setField(userValidator, "userRepository", userRepository);
        ReflectionTestUtils.setField(userValidator, "beanValidator", beanValidator);
    }

    @Test
    public void validateBeanSuccess() {
        Set<ConstraintViolation<User>> violations = new TreeSet<>();
        when(beanValidator.validate(any(User.class))).thenReturn(violations);

        userValidator.validateBean(new User());

        verify(beanValidator, times(1)).validate(any(User.class));
        verifyNoMoreInteractions(beanValidator);
    }

    @Test
    public void validateBeanFail() {
        ConstraintViolation<User> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<User>> violations = new HashSet<>();
        violations.add(violation);
        when(beanValidator.validate(any(User.class))).thenReturn(violations);
        when(violation.getMessage()).thenReturn(anyString());
        exception.expect(UserException.class);

        userValidator.validateBean(new User());

        verify(beanValidator, times(1)).validate(any(User.class));
        verifyNoMoreInteractions(beanValidator);

    }

    @Test
    public void validateFieldsNotEmptySuccess() {
        User user = StubFactory.getStub("singleUser", User.class);
        userValidator.validateFieldsNotEmpty(user, "id", "username", "password");
    }

    @Test
    public void validateFieldsNotEmptyIsNullFiled() {
        User user = new User();
        user.setId("id");
        user.setUsername("name");
        user.setPassword(null);
        exception.expect(UserException.class);

        userValidator.validateFieldsNotEmpty(user, "id","username", "password");
    }

    @Test
    public void validateFieldsNotEmptyIsEmptyFiled() {
        User user = new User();
        user.setId("id");
        user.setUsername("name");
        user.setPassword("");
        exception.expect(UserException.class);

        userValidator.validateFieldsNotEmpty(user, "id","username", "password");
    }

    @Test
    public void validateUserIdExistsSuccess() {
        when(userRepository.find(any(User.class))).thenReturn(StubFactory.getStub("singleUser", User.class));

        userValidator.validateUserIdExists(new User());

        verify(userRepository, times(1)).find(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void validateUserIdExistsFail() {
        when(userRepository.find(any(User.class))).thenReturn(null);
        exception.expect(UserException.class);

        userValidator.validateUserIdExists(new User());

        verify(userRepository, times(1)).find(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void validateUserMatchesExistingUserSuccess() {
        User userStub = StubFactory.getStub("singleUser", User.class);
        when(userRepository.find(userStub)).thenReturn(userStub);

        userValidator.validateUserMatchesExistingUser(userStub);

        verify(userRepository, times(1)).find(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void validateUserMatchesExistingUserFail() {
        User userStub = StubFactory.getStub("singleUser", User.class);
        when(userRepository.find(userStub)).thenReturn(null);
        exception.expect(UserException.class);

        userValidator.validateUserMatchesExistingUser(userStub);

        verify(userRepository, times(1)).find(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    public void validateUserNameExistsSuccess() {
        when(userRepository.findUserName(any(User.class))).thenReturn(StubFactory.getStub("singleUser", User.class));
        exception.expect(UserException.class);

        userValidator.validateUserNameExists(new User());

        verify(userRepository, times(1)).findUserName(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void validateUserNameExistsFail() {
        when(userRepository.findUserName(any(User.class))).thenReturn(null);

        userValidator.validateUserNameExists(new User());

        verify(userRepository, times(1)).findUserName(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}