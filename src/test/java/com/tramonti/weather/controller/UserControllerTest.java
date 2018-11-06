package com.tramonti.weather.controller;

import com.tramonti.weather.utils.TestUtils;
import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.service.UserService;
import com.tramonti.weather.validator.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tramonti.weather.domain.exception.WeatherException.Level.WARNING;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserValidator userValidator;

    @Test
    public void getAllUsersSuccessTest() throws Exception {
        List<User> stubUsers = Arrays.asList(TestUtils.getStub("users", User[].class));
        when(userService.findAll()).thenReturn(stubUsers);

        mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubUsers)));
    }

    @Test
    public void getAllUsersIsEmptyTest() throws Exception {
        when(userService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[]"));
    }

    @Test
    public void findUserByIdSuccessTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        when(userService.find(stubUser.getId())).thenReturn(stubUser);

        mockMvc.perform(get("/users/" + stubUser.getId())).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubUser)));
    }

    @Test
    public void findUserByIdNoUserTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        when(userService.find(stubUser.getId())).thenThrow(new UserException()
                .setClassName(this.getClass().getName())
                .setLevel(WARNING)
                .setName("No matching Id")
                .setDescription("user with such id does not exist")
                .setStatus(HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/users/" + stubUser.getId())).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"400\",\"name\":\"No matching Id\",\"description\":\"user with such id does not exist\"}"));
    }

    @Test
    public void createUserSuccessTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        when(userService.create(any(User.class))).thenReturn(stubUser);

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(new User()))
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubUser)));
    }

    @Test
    public void createUserAlreadyExistsTest() throws Exception {
        doThrow(new UserException()
                .setLevel(UserException.Level.WARNING)
                .setStatus(HttpStatus.FORBIDDEN)
                .setName("user already exists")
                .setDescription("user with such username already exists")
        ).when(userValidator).validateUserNameExists(any(User.class));

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(new User()))
        )
                .andDo(print()).andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"403\",\"name\":\"user already exists\",\"description\":\"user with such username already exists\"}"));
    }

    @Test
    public void createUserNotValidParametersTest() throws Exception {
        doThrow(new UserException()
                .setLevel(UserException.Level.WARNING)
                .setStatus(HttpStatus.BAD_REQUEST)
                .setName("business rules violation")
                .setDescription("some fields are empty")
        ).when(userValidator).validateFieldsNotEmpty(any(User.class), anyString(), anyString());

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(new User()))
        )
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"400\",\"name\":\"business rules violation\",\"description\":\"some fields are empty\"}"));
    }

    @Test
    public void updateUserSuccessTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        when(userService.update(any(User.class))).thenReturn(stubUser);

        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(stubUser))
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubUser)));
    }

    @Test
    public void updateUserNotExistsTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        doThrow(new UserException()
                .setLevel(UserException.Level.WARNING)
                .setStatus(HttpStatus.FORBIDDEN)
                .setName("user does not exist")
                .setDescription("there is no user with id " + stubUser.getId())
        ).when(userValidator).validateUserIdExists(stubUser);

        mockMvc.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(stubUser))
        )
                .andDo(print()).andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"403\",\"name\":\"user does not exist\",\"description\":\"there is no user with id 5bd3052361560103a88ede1d\"}"));
    }

    @Test
    public void deleteUserSuccessTest() throws Exception {
        User stubUser = TestUtils.getStub("singleUser", User.class);
        when(userService.delete(any(User.class))).thenReturn(stubUser);

        mockMvc.perform(delete("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(new User()))
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubUser)));
    }

    @Test
    public void deleteNotExistingUserTest() throws Exception {
        doThrow(new UserException()
                .setLevel(UserException.Level.WARNING)
                .setStatus(HttpStatus.FORBIDDEN)
                .setName("user does not exist")
                .setDescription("entered user does not match existing user")
        ).when(userValidator).validateUserMatchesExistingUser(any(User.class));

        mockMvc.perform(delete("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.formJsonString(new User()))
        )
                .andDo(print()).andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"403\",\"name\":\"user does not exist\",\"description\":\"entered user does not match existing user\"}"));
    }
}