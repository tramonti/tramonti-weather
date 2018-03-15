package com.tramonti.weather.repository;

import com.tramonti.weather.domain.User;

import java.util.List;

/**
 * Created by tarashrynchuk on 3/1/18.
 */
public interface UserRepository {

	List<User> findAll();

	User find(User user);

	User create(User user);

	User update(User user);

	User delete(User user);

	void deleteAll();

}
