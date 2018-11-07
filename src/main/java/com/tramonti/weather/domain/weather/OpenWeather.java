package com.tramonti.weather.domain.weather;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeather {

    @JsonAlias("cod")
    private String statusCode;
    private String message;
    @JsonAlias("list")
    private List<WeatherList> weatherList = null;
    private City city;

}
