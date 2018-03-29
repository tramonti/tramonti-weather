package com.tramonti.weather.service;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.domain.weather.Weather;
import com.tramonti.weather.domain.weather.WeatherList;
import com.tramonti.weather.repository.BroadcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BroadcastServiceImpl implements BroadcastService {

    private final BroadcastRepository broadcastRepository;

    @Autowired
    public BroadcastServiceImpl(BroadcastRepository broadcastRepository) {
        this.broadcastRepository = broadcastRepository;
    }

    @Override
    public List<BroadcastCity> extractFrom(OpenWeather openWeather) {
        String city = openWeather.getCity().getName().toLowerCase();
        List<WeatherList> weatherList = openWeather.getWeatherList();
        return weatherList.parallelStream()
                .map(weatherItem -> transform(weatherItem, city))
                .collect(Collectors.toList());
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
        return broadcastRepository.save(cities);
    }

    @Override
    public List<BroadcastCity> find(String cityName, LocalDate localDate) {
        return broadcastRepository.find(cityName, localDate);
    }
}
