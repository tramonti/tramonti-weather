package com.tramonti.weather.repository;

import com.tramonti.weather.domain.weather.OpenWeather;

public interface WeatherRepository {

    OpenWeather getWeather(String cityName);
}
