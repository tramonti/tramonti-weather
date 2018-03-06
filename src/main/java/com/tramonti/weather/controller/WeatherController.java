package com.tramonti.weather.controller;

import com.google.gson.Gson;
import com.tramonti.weather.domain.OpenWeather;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@RestController
public class WeatherController {

    @GetMapping("/weather")
    public OpenWeather getWeather(@RequestParam String city) {
        String urlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&units=metric&APPID=713d9a6a9e5940714f0f60d0f56a095d";
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
