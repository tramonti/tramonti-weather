package com.tramonti.weather.service;

import com.tramonti.weather.domain.OpenWeather;
import com.tramonti.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {

    private WeatherRepository weatherRepository;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @Override
    public OpenWeather getWeather(String cityName) {
        return weatherRepository.getWeather(cityName);
    }
}
