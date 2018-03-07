package com.tramonti.weather.controller;

import com.tramonti.weather.domain.OpenWeather;
import com.tramonti.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public OpenWeather getWeather(@RequestParam String city) {
        return weatherService.getWeather(city);
    }
}
