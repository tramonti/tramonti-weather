package com.tramonti.weather.controller;

import com.tramonti.weather.domain.User;
import com.tramonti.weather.domain.WeatherException;
import com.tramonti.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by tarashrynchuk on 3/1/18.
 */
@RestController()
public class UserController {
    @Autowired
    private UserService userService;

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
        if (user.getId() == null || user.getId().length() == 0) {
            throw new WeatherException()
                    .setDescription("user id cannot be empty")
                    .setName("Null pointer")
                    .setLevel(WeatherException.Level.WARNING)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setThrowable(new NullPointerException());
        }
        return userService.update(user);
    }

    @DeleteMapping(value = "/users", consumes = "application/json")
    public User deleteUser(@RequestBody User user) {
        if (user.getId() == null || user.getId().length() == 0) {
            throw new WeatherException()
                    .setDescription("user id cannot be empty")
                    .setName("Null pointer")
                    .setLevel(WeatherException.Level.WARNING)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setThrowable(new NullPointerException());
        }
        User userToDelete = userService.find(user.getId());
        if (user.equals(userToDelete)) {
            return userService.delete(user);
        } else {
            throw new WeatherException()
                    .setDescription("User with such parameters does not exist")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setLevel(WeatherException.Level.WARNING)
                    .setName("Validation rules")
                    .setThrowable(new IllegalAccessError());
        }

    }

    //TODO: get user by  username and

    //TODO: use loggers

    //@ExceptionHandler - map exception to http error
    //@ControllerAdvice

}
