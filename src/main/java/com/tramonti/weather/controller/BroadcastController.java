package com.tramonti.weather.controller;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.hateaos.HateaosResource;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.service.BroadcastService;
import com.tramonti.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/broadcast")
public class BroadcastController {

    private final BroadcastService broadcastService;

    private final WeatherService weatherService;

    @Autowired
    public BroadcastController(BroadcastService broadcastService, WeatherService weatherService) {
        this.broadcastService = broadcastService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public HateaosResource<List<String>> getCities() {
        List<String> cities = broadcastService.findAvailableCities();
        String firstCity = cities.get(0);

        HateaosResource<List<String>> cityListResource = new HateaosResource<>(cities);
        Link selfLink = linkTo(methodOn(BroadcastController.class).getCities())
                .withSelfRel()
                .withTitle("getCity")
                .withHreflang("eng");
        Link storeCity = linkTo(methodOn(BroadcastController.class).storeCity(firstCity))
                .withRel("saveOrUpdateCity")
                .withHreflang("eng");
        Link firstCityLink = linkTo(methodOn(BroadcastController.class).getCityByDay(firstCity, LocalDate.now()))
                .withRel("getCityWeather")
                .withHreflang("eng");

        cityListResource.add(selfLink, storeCity, firstCityLink);

        return cityListResource;
    }

    @GetMapping("/store")
    public HateaosResource<List<BroadcastCity>> storeCity(@RequestParam("city") String cityName) {
        OpenWeather openWeather = weatherService.getWeather(cityName);
        List<BroadcastCity> broadcastCities = broadcastService.extractFrom(openWeather);
        broadcastService.save(broadcastCities);

        HateaosResource<List<BroadcastCity>> resources = new HateaosResource<>();
        resources.setResource(broadcastCities);
        resources.add(linkTo(methodOn(BroadcastController.class).storeCity(cityName)).withSelfRel());
        resources.add(linkTo(methodOn(BroadcastController.class).getCityByDay(openWeather.getCity().getName(), LocalDate.now()))
                .withRel("getTodayCityWeather"));
        resources.add(linkTo(methodOn(WeatherController.class).getWeather(cityName)).withRel("getCompleteCityWeather"));
        return resources;
    }

    @GetMapping("/{city}")
    public HateaosResource<List<BroadcastCity>> getCityByDay(@PathVariable("city") String cityName,
                                        @RequestParam
                                          @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        HateaosResource<List<BroadcastCity>> resource = new HateaosResource<>();
        cityName = cityName.toLowerCase();
        boolean nextDayPresent = broadcastService.exists(cityName, date.plusDays(1));
        boolean prevDayPresent = broadcastService.exists(cityName, date.minusDays(1));

        resource.add(linkTo(methodOn(BroadcastController.class).getCityByDay(cityName, date)).withSelfRel());

        if (nextDayPresent) {
            resource.add(linkTo(methodOn(BroadcastController.class).getCityByDay(cityName, date.plusDays(1))).withRel("nextDay"));
        }

        if (prevDayPresent) {
            resource.add(linkTo(methodOn(BroadcastController.class).getCityByDay(cityName, date.minusDays(1))).withRel("prevDay"));
        }
        resource.setResource(broadcastService.find(cityName, date));
        return resource;
    }
}
