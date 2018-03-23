package com.tramonti.weather.repository;

import com.tramonti.weather.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private MongoTemplate mongoTemplate;

    @Autowired
    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public User find(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        return mongoTemplate.findOne(query, User.class, "users");
    }

    @Override
    public User create(User user) {
        mongoTemplate.insert(user);
        return user;
    }

    @Override
    public User update(User user) {
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public User delete(User user) {
        mongoTemplate.remove(user);
        return user;
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), "users");
    }

    @Override
    public User findUserName(User user) {
        Query query = new Query(Criteria.where("username").is(user.getUsername()));
        return mongoTemplate.findOne(query, User.class, "users");
    }


}
