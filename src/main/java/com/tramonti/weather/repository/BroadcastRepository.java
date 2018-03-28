package com.tramonti.weather.repository;

import com.tramonti.weather.domain.broadcast.BroadcastCity;

import java.util.List;

public interface BroadcastRepository {

    List<BroadcastCity> save(List<BroadcastCity> cities);
}
