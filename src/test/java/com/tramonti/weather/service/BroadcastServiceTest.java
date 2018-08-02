package com.tramonti.weather.service;

import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.repository.BroadcastRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE) // not load Controllers
public class BroadcastServiceTest {

    @Mock
    private BroadcastRepository broadcastRepository;

    private BroadcastService broadcastService;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp(){
        broadcastService = new BroadcastServiceImpl(broadcastRepository);
    }

    @Test
    public void extractBroadcastCityListFromOpenWeatherTest() {
        OpenWeather openWeather = mock(OpenWeather.class);

    }

    @Test
    public void saveBroadcastCityListInDBTest() {
    }

    @Test
    public void findBroadcastCityListByNameAndDateInDBTest() {
    }

    @Test
    public void existsBroadcastCityInDBTest() {
    }

    @Test
    public void findAvailableCitiesInDBTest() {
    }
}