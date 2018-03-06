package com.tramonti.weather.repository;

import com.tramonti.weather.domain.User;

import java.util.List;

/**
 * Created by tarashrynchuk on 3/1/18.
 */
public interface UserRepository {

	List<User> getUsers();

}
