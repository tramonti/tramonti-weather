package com.tramonti.weather.repository;

import com.tramonti.weather.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@Repository
public class UserRepositoryImpl implements UserRepository {
	@Override
	public List<User> getUsers() {

		User user = new User();
		user.setUsername("user 1");

		return asList(user);

	}
}
