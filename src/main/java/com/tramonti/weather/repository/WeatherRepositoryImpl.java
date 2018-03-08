package com.tramonti.weather.repository;


import com.google.gson.Gson;
import com.tramonti.weather.domain.OpenWeather;
import com.tramonti.weather.domain.WeatherException;
import lombok.Cleanup;
import org.springframework.http.HttpStatus;
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
        } catch (IOException e) {
            throw new WeatherException()
                    .setDescription("city not found")
                    .setName("Illegal City Name")
                    .setThrowable(e)
                    .setLevel(WeatherException.Level.ERROR)
                    .setStatus(HttpStatus.NOT_FOUND);
        }
        return openWeather;
    }
}
