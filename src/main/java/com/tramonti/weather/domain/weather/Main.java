package com.tramonti.weather.domain.weather;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Main {

    private Double temp;
    @JsonAlias("temp_min")
    private Double tempMin;
    @JsonAlias("temp_max")
    private Double tempMax;
    private Double pressure;
    private Integer humidity;

}
