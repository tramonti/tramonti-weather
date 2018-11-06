package com.tramonti.weather.repository;

import com.tramonti.weather.utils.TestUtils;
import com.tramonti.weather.domain.user.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    private UserRepository userRepository;

    private List<User> stubUsers;

    @Mock
    private MongoTemplate mongoTemplate;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepositoryImpl(mongoTemplate);
        stubUsers = new ArrayList(Arrays.asList(TestUtils.getStub("Users", User[].class)));
    }

    @Test
    public void findAll() {
        when(mongoTemplate.findAll(User.class)).thenReturn(stubUsers);

        List<User> result = userRepository.findAll();

        verify(mongoTemplate, times(1)).findAll(User.class);
        verifyNoMoreInteractions(mongoTemplate);
        assertEquals(stubUsers.get(0), result.get(0));
        assertEquals(stubUsers.size(), result.size());
    }


    @Test
    public void find() {
        User userToFind = new User();
        userToFind.setId(stubUsers.get(0).getId());
        Query query = new Query(Criteria.where("_id").is(userToFind.getId()));
        when(mongoTemplate.findOne(query, User.class, "users")).thenReturn(stubUsers.get(0));

        User result = userRepository.find(userToFind);
        verify(mongoTemplate, times(1)).findOne(query, User.class, "users");
        verifyNoMoreInteractions(mongoTemplate);
        assertEquals(stubUsers.get(0), result);
    }

    @Test
    public void create() {
        userRepository.create(new User());
        verify(mongoTemplate, times(1)).insert(any(User.class));
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void update() {
        userRepository.update(new User());
        verify(mongoTemplate, times(1)).save(any(User.class));
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void delete() {
        userRepository.delete(new User());
        verify(mongoTemplate, times(1)).remove(any(User.class));
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void deleteAll() {
        userRepository.deleteAll();
        verify(mongoTemplate, times(1)).remove(new Query(),"users");
        verifyNoMoreInteractions(mongoTemplate);
    }

    @Test
    public void findUserName() {
        Query query = new Query(Criteria.where("username").is(stubUsers.get(0).getUsername()));
        when(mongoTemplate.findOne(query, User.class, "users")).thenReturn(stubUsers.get(0));

        User result = userRepository.findUserName(stubUsers.get(0));

        verify(mongoTemplate, times(1)).findOne(query, User.class, "users");
        verifyNoMoreInteractions(mongoTemplate);
        assertEquals(stubUsers.get(0), result);
    }
}