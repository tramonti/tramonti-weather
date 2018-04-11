package com.tramonti.weather.service;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.weather.OpenWeather;

import java.time.LocalDate;
import java.util.List;

public interface BroadcastService {

    List<BroadcastCity> extractFrom(OpenWeather openWeather);

    List<BroadcastCity> save(List<BroadcastCity> cities);

    List<BroadcastCity> find(String cityName, LocalDate localDate);

    boolean exists(String cityName, LocalDate localDate);

    List<String> findAvailableCities();
}
