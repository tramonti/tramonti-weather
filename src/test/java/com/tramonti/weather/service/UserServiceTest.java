package com.tramonti.weather.service;

import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.repository.UserRepository;
import com.tramonti.weather.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl userService;

    private List<User> stubUsers;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        stubUsers = new ArrayList<>(Arrays.asList(TestUtils.getStub("users", User[].class)));
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void findAllSuccessTest() {
        when(userRepository.findAll()).thenReturn(stubUsers);

        List<User> testUsers = userService.findAll();

        verify(userRepository, only()).findAll();
        assertEquals(testUsers, stubUsers);
    }

    @Test
    public void findAllEmptyTest() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> testUsers = userService.findAll();

        verify(userRepository, only()).findAll();
        assertEquals(0, testUsers.size());
        assertNotNull(testUsers);

    }

    @Test
    public void findSuccessTest() {
        when(userRepository.find(any())).thenReturn(stubUsers.get(0));

        User testUser = userService.find("63df2fds8963khkcs232d32eds2d");

        verify(userRepository, only()).find(any());
        assertEquals(testUser, stubUsers.get(0));
    }

    @Test
    public void findFailTest() {
        when(userRepository.find(any())).thenReturn(null);
        exception.expect(UserException.class);

        userService.find("63df2fds8963khkcs232d32eds2d");

        verify(userRepository, only()).find(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void createSuccessTest() {
        User userToCreate = new User();
        userToCreate.setUsername("Qwe");
        userToCreate.setPassword("haslfdhaf");
        when(userRepository.create(userToCreate)).thenReturn(stubUsers.get(0));

        User testUser = userService.create(userToCreate);
        verify(userRepository, only()).create(any());
        assertEquals(testUser, stubUsers.get(0));

    }

    @Test
    public void updateSuccessTest() {
        User userToUpdate = stubUsers.get(0);
        userToUpdate.setPassword("qwertyqwerty");
        when(userRepository.update(userToUpdate)).thenReturn(userToUpdate);

        User updatedUser = userService.update(userToUpdate);

        verify(userRepository, only()).update(any());
        assertNotNull(updatedUser);
        assertEquals(updatedUser, userToUpdate);
    }

    @Test
    public void updateNullPropertiesTest() {
        User userToUpdate = new User();
        doAnswer((in) -> {
            User user = new User();
            user.setId("5b6ab4466d4c583c1c7ba731");
            return user;
        }).when(userRepository).update(userToUpdate);

        User updatedUser = userService.update(userToUpdate);

        verify(userRepository, only()).update(any());
        assertNotNull(updatedUser);
        assertNull(updatedUser.getUsername());
        assertNull(updatedUser.getPassword());
        assertEquals("5b6ab4466d4c583c1c7ba731", updatedUser.getId());
    }

    @Test
    public void deleteSuccessTest() {
        User deletedUser = stubUsers.get(1);

        doAnswer(in -> stubUsers.remove(1)).when(userRepository).delete(any());
        User userToDelete = new User();
        userToDelete.setId("63df2fds8963khkcs232d32eds2d");

        User testUser = userService.delete(userToDelete);

        verify(userRepository, only()).delete(any());
        assertEquals(1, stubUsers.size());
        assertEquals(testUser, deletedUser);

    }
}