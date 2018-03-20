package com.tramonti.weather.service;

import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
 public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User find(String id) {
		User user = new User();
		user.setId(id);
		return userRepository.find(user);
	}

	@Override
	public User create(User user) {
		return userRepository.create(user);
	}

	@Override
	public User update(User user) {
		return userRepository.update(user);
	}

	@Override
	public User delete(User user) {
		return userRepository.delete(user);
	}
}
