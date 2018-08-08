package com.tramonti.weather.controller;

import com.tramonti.weather.UserStubFactory;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.service.UserService;
import com.tramonti.weather.validator.UserValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserService userService;

    private List<User> stubUsers;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        userController = new UserController(userService, userValidator);
        stubUsers = UserStubFactory.getStubUsers();
    }

    @Test
    public void getAllUsersTest() {
        when(userService.findAll()).thenReturn(stubUsers);

        List<User> testUsers = userController.getAllUsers();

        assertEquals(stubUsers, testUsers);
        verify(userService, only()).findAll();
    }

    @Test
    public void getAllUsersIsEmptyTest() {
        when(userService.findAll()).thenReturn(new ArrayList<>());

        List<User> testUsers = userController.getAllUsers();

        assertTrue(testUsers.isEmpty());
        verify(userService, only()).findAll();
    }

    @Test
    public void findUserByIdSuccessTest() {
        String userId = stubUsers.get(0).getId();
        when(userService.find(userId)).thenReturn(stubUsers.get(0));

        User testUser = userController.findUserById(userId);

        assertEquals(stubUsers.get(0), testUser);
        verify(userService, only()).find(anyString());
    }

    @Test
    public void findUserByIdNotFoundTest() {
    }

    @Test
    public void createUserSuccessTest() {
    }

    @Test
    public void createUserAlreadyExistsTest() {
    }

    @Test
    public void createUserNotValidParametersTest() {
    }

    @Test
    public void updateUserSuccessTest() {
    }

    @Test
    public void updateUserNotExistsTest() {
    }

    @Test
    public void deleteUserSuccessTest() {
    }

    @Test
    public void deleteUserNotExistTest() {
    }
}