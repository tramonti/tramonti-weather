package com.tramonti.weather.controller;

import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.service.UserService;
import com.tramonti.weather.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    private UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable String id) {
        return userService.find(id);
    }

    @PutMapping(value = "/users", consumes = "application/json")
    public User createUser(@RequestBody User user) {
        userValidator.validateFieldsNotEmpty(user, "username", "password");
        userValidator.validateUserNameExists(user);
        return userService.create(user);
    }

    @PostMapping(value = "/users/update", consumes = "application/json")
    public User updateUser(@RequestBody User user) {
        userValidator.validateBean(user);
        userValidator.validateUserIdExists(user);
        return userService.update(user);
    }

    @DeleteMapping(value = "/users", consumes = "application/json")
    public User deleteUser(@RequestBody User user) {
        userValidator.validateBean(user);
        userValidator.validateUserMatchesExistingUser(user);
        User userToDelete = userService.find(user.getId());
        return userService.delete(user);
    }

}
