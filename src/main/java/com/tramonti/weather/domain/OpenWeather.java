
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeather {

    @SerializedName("cod")
    @Expose
    private String statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("list")
    @Expose
    private List<WeatherList> weatherList = null;
    @SerializedName("city")
    @Expose
    private City city;

}
