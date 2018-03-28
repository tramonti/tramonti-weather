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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/broadcast")
public class BroadcastController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public List<String> getCities() {
        return Collections.singletonList("lviv");
    }

    @GetMapping("/store")
    public List<BroadcastCity> storeCity(@RequestParam("city") String cityName) {
        OpenWeather openWeather = weatherService.getWeather(cityName);
        return broadcastService.extractFrom(openWeather);
    }

    @GetMapping("/{city}")
    public LocalDate getCityByDay(@PathVariable("city") String cityName, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return date;
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
