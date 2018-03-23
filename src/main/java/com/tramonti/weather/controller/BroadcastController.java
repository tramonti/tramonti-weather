package com.tramonti.weather.controller;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
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

    @GetMapping
    public List<String> getCities() {
        return Collections.singletonList("lviv");
    }

    @GetMapping("/store")
    public BroadcastCity storeCity(@RequestParam("city") String cityName) {
        BroadcastCity broadcastCity = new BroadcastCity();
        broadcastCity.setCity(cityName)
                .setDescription("cloudy")
                .setIcon("icon")
                .setTemperature("32")
                .setDateTime(LocalDateTime.now());
        mongoTemplate.insert(broadcastCity);
        return broadcastCity;
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
