package com.tramonti.weather.domain.weather;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class WeatherList {

    private Main main;
    private java.util.List<Weather> weather = null;
    private Clouds clouds;
    private Wind wind;
    @JsonAlias("dt_txt")
    private String dateTxt;

}
