
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class OpenWeather {

    @SerializedName("cod")
    @Expose
    public String statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("list")
    @Expose
    public List<WeatherList> weatherList = null;
    @SerializedName("city")
    @Expose
    public City city;

}
