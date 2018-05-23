package com.tramonti.weather.domain.broadcast;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class BroadcastResource extends ResourceSupport {
    private List<BroadcastCity> weather;

    public BroadcastResource() {
    }

    public BroadcastResource(List<BroadcastCity> weather) {
        this.weather = weather;
    }

    public List<BroadcastCity> getWeather() {
        return weather;
    }

    public BroadcastResource setWeather(List<BroadcastCity> weather) {
        this.weather = weather;
        return this;
    }
}
