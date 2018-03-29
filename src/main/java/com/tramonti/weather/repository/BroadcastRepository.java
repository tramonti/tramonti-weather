package com.tramonti.weather.repository;

import com.tramonti.weather.domain.broadcast.BroadcastCity;

import java.time.LocalDate;
import java.util.List;

public interface BroadcastRepository {

    List<BroadcastCity> save(List<BroadcastCity> cities);

    List<BroadcastCity> find(String cityName, LocalDate date);
}
