package com.tramonti.weather.controller;

import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.service.UserService;
import com.tramonti.weather.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tarashrynchuk on 3/1/18.
 */
@RestController()
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    //TODO: crud users operations
    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable String id) {
        return userService.find(id);
    }

    @PutMapping(value = "/users", consumes = "application/json")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PostMapping(value = "/users/update", consumes = "application/json")
    public User updateUser(@RequestBody User user) {
        userValidator.validateId(user);
        return userService.update(user);
    }

    @DeleteMapping(value = "/users", consumes = "application/json")
    public User deleteUser(@RequestBody User user) {
        userValidator.validateId(user);
        User userToDelete = userService.find(user.getId());
        userValidator.validateAllUser(user, userToDelete);
        return userService.delete(user);
    }

    //TODO: get user by  username and

    //TODO: use loggers

    //@ExceptionHandler - map exception to http error
    //@ControllerAdvice

}
