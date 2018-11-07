package com.tramonti.weather.service;

import com.tramonti.weather.domain.exception.CityNotFoundException;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.utils.TestUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    private WeatherService weatherService;

    @Mock
    private RestTemplate restTemplate;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        weatherService = new WeatherServiceImpl();
        ReflectionTestUtils.setField(weatherService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(weatherService, "weatherUrl", "http://api.openweathermap.org/data/2.5/forecast");
        ReflectionTestUtils.setField(weatherService, "APIKey", "713d9a6a9e5940714f0f60d0f56a095d");
        ReflectionTestUtils.setField(weatherService, "temperatureUnits", "metric");
    }

    @Test
    public void getWeatherSuccess() throws URISyntaxException {
        OpenWeather weatherStub = TestUtils.getStub("London", OpenWeather.class);
        URI uri = new URI("http://api.openweathermap.org/data/2.5/forecast?q=London&units=metric&APPID=713d9a6a9e5940714f0f60d0f56a095d");
        when(restTemplate.getForObject(uri, OpenWeather.class))
                .thenReturn(weatherStub);

        OpenWeather result = weatherService.getWeather("London");

        verify(restTemplate, times(1)).getForObject(uri, OpenWeather.class);
        assertEquals("London", result.getCity().getName());
        assertEquals(40, result.getWeatherList().size());
        assertEquals("200", result.getStatusCode());
        assertEquals("2018-10-26 12:00:00", result.getWeatherList().get(1).getDateTxt());
        assertEquals("light rain", result.getWeatherList().get(1).getWeather().get(0).getDescription());
    }

    @Test
    public void getWeatherFail() throws URISyntaxException {
        URI uri = new URI("http://api.openweathermap.org/data/2.5/forecast?q=LVL&units=metric&APPID=713d9a6a9e5940714f0f60d0f56a095d");
        when(restTemplate.getForObject(uri, OpenWeather.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        exception.expect(CityNotFoundException.class);

        weatherService.getWeather("LVL");
    }
}