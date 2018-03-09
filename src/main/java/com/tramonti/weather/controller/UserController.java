package com.tramonti.weather.controller;

import com.tramonti.weather.domain.User;
import com.tramonti.weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return  userService.findAll();
	}

	//TODO: crud users operations

	//TODO: get user by  username and

	//TODO: use loggers

	//@ExceptionHandler - map exception to http error
	//@ControllerAdvice


}
