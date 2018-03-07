package com.tramonti.weather.repository;


import com.google.gson.Gson;
import com.tramonti.weather.domain.OpenWeather;
import lombok.Cleanup;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {

    @Override
    public OpenWeather getWeather(String cityName) {
        String urlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&units=metric&APPID=713d9a6a9e5940714f0f60d0f56a095d";
        OpenWeather openWeather;
        try {
            URL url = new URL(urlString);
            @Cleanup InputStreamReader reader = new InputStreamReader(url.openStream());
            openWeather = new Gson().fromJson(reader, OpenWeather.class);
            System.out.println(openWeather);
        } catch (IOException e) {
            openWeather = new OpenWeather();
            openWeather.setStatusCode("404");
            openWeather.setMessage("City not found");
        }
        return openWeather;
    }
}
