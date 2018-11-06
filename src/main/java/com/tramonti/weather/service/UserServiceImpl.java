package com.tramonti.weather.service;

import com.tramonti.weather.domain.exception.UserException;
import com.tramonti.weather.domain.user.User;
import com.tramonti.weather.repository.UserRepository;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tramonti.weather.domain.exception.WeatherException.Level.WARNING;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User find(String id) {
        User userToFind = new User();
        userToFind.setId(id);
        User result = userRepository.find(userToFind);
        if (result == null) {
            NDC.push("user: " + userToFind);
            throw new UserException()
                    .setClassName(this.getClass().getName())
                    .setLevel(WARNING)
                    .setName("No matching Id")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setDescription("user with such id does not exist");
        }else {
            return result;
        }
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
