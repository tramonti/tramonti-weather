package com.tramonti.weather.domain.broadcast;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

public class CityListResource extends ResourceSupport {

    private List<String> availableCities;

    public CityListResource() {
    }

    public CityListResource(List<String> cities) {
        this.availableCities = cities;
    }

    public List<String> getAvailableCities() {
        return availableCities;
    }

    public CityListResource setAvailableCities(List<String> availableCities) {
        this.availableCities = availableCities;
        return this;
    }
}
