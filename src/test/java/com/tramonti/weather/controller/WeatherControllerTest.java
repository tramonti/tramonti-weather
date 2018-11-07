package com.tramonti.weather.controller;

import com.tramonti.weather.domain.exception.CityNotFoundException;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.service.WeatherService;
import com.tramonti.weather.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void getWeatherSuccessTest() throws Exception {
        OpenWeather stubWeather = TestUtils.getStub("London", OpenWeather.class);
        when(weatherService.getWeather("London")).thenReturn(stubWeather);

        mockMvc.perform(get("/weather?city=London")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(stubWeather)));
    }

    @Test
    public void getWeatherFailTest() throws Exception {
        when(weatherService.getWeather(anyString())).thenThrow(new CityNotFoundException()
                .setDescription("city not found")
                .setName("Illegal City Name")
                .setLevel(CityNotFoundException.Level.ERROR)
                .setStatus(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/weather?city=Lsad")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"404\",\"name\":\"Illegal City Name\",\"description\":\"city not found\"}"));
    }
}