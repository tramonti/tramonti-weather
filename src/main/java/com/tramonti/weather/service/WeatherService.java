package com.tramonti.weather.service;

import com.tramonti.weather.domain.weather.OpenWeather;

public interface WeatherService {

    OpenWeather getWeather(String cityName);
}
