package com.tramonti.weather.service;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.domain.weather.Weather;
import com.tramonti.weather.domain.weather.WeatherList;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BroadcastServiceImpl implements BroadcastService {
    @Override
    public List<BroadcastCity> extractFrom(OpenWeather openWeather) {
        String city = openWeather.getCity().getName();
        List<WeatherList> weatherList = openWeather.getWeatherList();
        List<BroadcastCity> broadcastCities = weatherList.parallelStream().map(weatherItem-> transform(weatherItem, city)).collect(Collectors.toList());
        return broadcastCities;
    }

    private BroadcastCity transform(WeatherList weatherItem, String city) {
        BroadcastCity broadcastCity = new BroadcastCity();
        Weather weatherInfo = weatherItem.getWeather().get(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        LocalDateTime weatherDateTime = LocalDateTime.parse(weatherItem.getDateTxt(), formatter);
        broadcastCity.setCity(city)
                .setTemperature(weatherItem.getMain().getTemp())
                .setIcon(weatherInfo.getIcon())
                .setDescription(weatherInfo.getDescription())
                .setDateTime(weatherDateTime);
        return broadcastCity;
    }

    @Override
    public List<BroadcastCity> save(List<BroadcastCity> cities) {
        return null;
    }
}
