package com.tramonti.weather.controller;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.service.BroadcastService;
import com.tramonti.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/broadcast")
public class BroadcastController {

    private final MongoTemplate mongoTemplate;

    private final BroadcastService broadcastService;

    private final WeatherService weatherService;

    @Autowired
    public BroadcastController(MongoTemplate mongoTemplate, BroadcastService broadcastService, WeatherService weatherService) {
        this.mongoTemplate = mongoTemplate;
        this.broadcastService = broadcastService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public List<String> getCities() {
        return Collections.singletonList("lviv");
    }

    @GetMapping("/store")
    public List<BroadcastCity> storeCity(@RequestParam("city") String cityName) {
        OpenWeather openWeather = weatherService.getWeather(cityName);
        List<BroadcastCity> broadcastCities = broadcastService.extractFrom(openWeather);
        return broadcastService.save(broadcastCities);
    }

    @GetMapping("/{city}")
    public List<BroadcastCity> getCityByDay(@PathVariable("city") String cityName,
                                  @RequestParam
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        cityName = cityName.toLowerCase();
        return broadcastService.find(cityName, date);
    }


    @GetMapping("/{city}/next")
    public List<BroadcastCity> getNextCity(@PathVariable String city) {
        Query query = new Query(Criteria.where("city").is(city));
        return mongoTemplate.find(query, BroadcastCity.class);
    }

    @GetMapping("/{city}/prev")
    public void getPreviousCity() {
    }
}
