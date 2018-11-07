package com.tramonti.weather.service;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.repository.BroadcastRepository;
import com.tramonti.weather.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public void setUp() {
        broadcastService = new BroadcastServiceImpl(broadcastRepository);
    }

    @Test
    public void extractBroadcastCityListFromOpenWeatherTest() {
        OpenWeather openWeatherStub = TestUtils.getStub("London", OpenWeather.class);

        List<BroadcastCity> result = broadcastService.extractFrom(openWeatherStub);

        assertEquals(40, result.size());
        assertEquals(new BroadcastCity()
                        .setCity("london")
                        .setTemperature(10.34)
                        .setDescription("light rain")
                        .setIcon("10d")
                        .setDateTime(LocalDateTime.of(
                                LocalDate.of(2018, 10, 26), LocalTime.NOON)),
                result.get(1));
    }

    @Test
    public void saveBroadcastCityListInDBTest() {
        broadcastService.save(anyList());
        verify(broadcastRepository, atMost(1)).save(anyList());
        verifyNoMoreInteractions(broadcastRepository);
    }

    @Test
    public void findBroadcastCityListByNameAndDateInDBTest() {
        when(broadcastRepository.find("London", LocalDate.now()))
                .thenReturn(Collections.singletonList(new BroadcastCity()
                        .setId("someId123")
                        .setCity("London")
                        .setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.NOON))));

        BroadcastCity result = broadcastService.find("London", LocalDate.now()).get(0);

        verify(broadcastRepository, atMost(1)).find("London", LocalDate.now());
        verifyNoMoreInteractions(broadcastRepository);
        assertEquals(new BroadcastCity()
                .setId("someId123")
                .setCity("London")
                .setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.NOON)), result);
    }

    @Test
    public void existsBroadcastCityInDBTest() {
        when(broadcastRepository.exists("London", LocalDate.now())).thenReturn(true);
        when(broadcastRepository.exists("Lviv", LocalDate.now())).thenReturn(false);

        boolean positiveResult = broadcastService.exists("London", LocalDate.now());
        boolean negativeResult = broadcastService.exists("Lviv", LocalDate.now());

        verify(broadcastRepository, atMost(1)).exists("London", LocalDate.now());
        verify(broadcastRepository, atMost(1)).exists("Lviv", LocalDate.now());
        verifyNoMoreInteractions(broadcastRepository);
        assertTrue(positiveResult);
        assertFalse(negativeResult);
    }

    @Test
    public void findAvailableCitiesInDBTest() {
        when(broadcastRepository.getAvailableCities()).thenReturn(Arrays.asList("London", "Kiev", "Brussels"));
        List<String> result = broadcastService.findAvailableCities();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("London", result.get(0));
        verify(broadcastRepository, atMost(1)).getAvailableCities();
    }
}