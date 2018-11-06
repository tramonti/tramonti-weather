package com.tramonti.weather.service;

import com.tramonti.weather.domain.exception.CityNotFoundException;
import com.tramonti.weather.domain.weather.OpenWeather;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${weatherUrl}")
    private String weatherUrl;

    @Value("${APIKey}")
    private String APIKey;

    @Value("${temperatureUnits}")
    private String temperatureUnits;

    @Override
    public OpenWeather getWeather(String cityName) {
        URI openWeatherForecastURL = UriComponentsBuilder
                .fromHttpUrl(weatherUrl)
                .queryParam("q", cityName)
                .queryParam("units", temperatureUnits)
                .queryParam("APPID", APIKey).build().toUri();

        OpenWeather openWeather;
        try {
            openWeather = restTemplate.getForObject(openWeatherForecastURL, OpenWeather.class);
        } catch (Exception e) {
            NDC.push("cityName = " + cityName);
            NDC.push("exception = " + e.toString());
            throw new CityNotFoundException()
                    .setDescription("city not found")
                    .setName("Illegal City Name")
                    .setLevel(CityNotFoundException.Level.ERROR)
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setClassName(this.getClass().getName());
        }
        return openWeather;
    }
}
