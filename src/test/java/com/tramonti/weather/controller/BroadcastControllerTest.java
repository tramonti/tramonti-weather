package com.tramonti.weather.controller;

import com.tramonti.weather.domain.broadcast.BroadcastCity;
import com.tramonti.weather.domain.exception.CityNotFoundException;
import com.tramonti.weather.domain.hateaos.HateaosResource;
import com.tramonti.weather.domain.weather.OpenWeather;
import com.tramonti.weather.service.BroadcastService;
import com.tramonti.weather.service.WeatherService;
import com.tramonti.weather.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(BroadcastController.class)
public class BroadcastControllerTest {

    private final static String LONDON = "London";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BroadcastService broadcastService;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void getCitiesSuccess() throws Exception {
        when(broadcastService.findAvailableCities()).thenReturn(Arrays.asList("London", "Lviv", "Kiev"));

        mockMvc.perform(get("/broadcast")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"resource\":[\"London\",\"Lviv\",\"Kiev\"]," +
                        "\"links\":[{" +
                        "\"rel\":\"self\"," +
                        "\"href\":\"http://localhost/broadcast\"," +
                        "\"hreflang\":\"eng\"," +
                        "\"media\":null," +
                        "\"title\":\"getCity\"," +
                        "\"type\":null," +
                        "\"deprecation\":null}," +
                        "{\"rel\":\"saveOrUpdateCity\"," +
                        "\"href\":\"http://localhost/broadcast/store?city=London\"," +
                        "\"hreflang\":\"eng\"," +
                        "\"media\":null," +
                        "\"title\":null," +
                        "\"type\":null," +
                        "\"deprecation\":null}," +
                        "{\"rel\":\"getCityWeather\"," +
                        "\"href\":\"http://localhost/broadcast/London?date=" + LocalDate.now() + "\"," +
                        "\"hreflang\":\"eng\"," +
                        "\"media\":null," +
                        "\"title\":null," +
                        "\"type\":null," +
                        "\"deprecation\":null}]}"));
    }

    @Test
    public void getCitiesFail() throws Exception {
        when(broadcastService.findAvailableCities()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/broadcast")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"resource\":[]," +
                        "\"links\":[{" +
                        "\"rel\":\"storeCity\"," +
                        "\"href\":\"http://localhost/broadcast/store?city=London\"," +
                        "\"hreflang\":\"eng\"," +
                        "\"media\":null," +
                        "\"title\":\"storeCity\"," +
                        "\"type\":null," +
                        "\"deprecation\":null}]}"));
    }

    @Test
    public void storeCitySuccessTest() throws Exception {
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>();
        List<BroadcastCity> stubCities = Arrays.asList(
                TestUtils.getStub("broadcasts", BroadcastCity[].class));
        expectedResult.setResource(stubCities);
        expectedResult.add(linkTo(methodOn(BroadcastController.class).storeCity(LONDON))
                .withSelfRel()
                .withHref("http://localhost/broadcast/store?city=" + LONDON));
        expectedResult.add(linkTo(methodOn(WeatherController.class).getWeather(LONDON))
                .withRel("getCompleteCityWeather")
                .withHref("http://localhost/weather?city=" + LONDON));
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withRel("getTodayCityWeather")
                .withHref("http://localhost/broadcast/" + LONDON + "?date=" + LocalDate.now()));

        when(weatherService.getWeather(LONDON)).thenReturn(TestUtils.getStub(LONDON, OpenWeather.class));
        when(broadcastService.extractFrom(any(OpenWeather.class))).thenReturn(stubCities);
        when(broadcastService.save(stubCities)).thenReturn(stubCities);

        mockMvc.perform(get("/broadcast/store?city=" + LONDON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

    @Test
    public void storeCityFailTest() throws Exception {
        doThrow(new CityNotFoundException()
                .setDescription("city not found")
                .setName("Illegal City Name")
                .setLevel(CityNotFoundException.Level.ERROR)
                .setStatus(HttpStatus.NOT_FOUND)).when(weatherService).getWeather(anyString());

        mockMvc.perform(get("/broadcast/store?city=" + LONDON)).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"status\":\"404\"," +
                        "\"name\":\"Illegal City Name\"," +
                        "\"description\":\"city not found\"}"));
    }

    @Test
    public void getCityByDaySuccessTest() throws Exception {
        List<BroadcastCity> stubCities = Arrays.asList(
                TestUtils.getStub("london-11-01", BroadcastCity[].class));
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>(stubCities);
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withSelfRel()
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now()));
        expectedResult.add(linkTo(methodOn(BroadcastController.class)
                .getCityByDay(LONDON, LocalDate.now().plusDays(1)))
                .withRel("nextDay")
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now().plusDays(1)));
        expectedResult.add(linkTo(methodOn(BroadcastController.class)
                .getCityByDay(LONDON, LocalDate.now().minusDays(1)))
                .withRel("prevDay")
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now().minusDays(1)));

        when(broadcastService.find(LONDON.toLowerCase(), LocalDate.now())).thenReturn(stubCities);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().plusDays(1))).thenReturn(true);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().minusDays(1))).thenReturn(true);

        mockMvc.perform(get("/broadcast/" + LONDON + "?date=" + LocalDate.now())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

    @Test
    public void getCityByDayFailTest() throws Exception {
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>(new ArrayList<>());
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withSelfRel()
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now()));

        when(broadcastService.find(LONDON.toLowerCase(), LocalDate.now())).thenReturn(new ArrayList<>());
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().plusDays(1))).thenReturn(false);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().minusDays(1))).thenReturn(false);

        mockMvc.perform(get("/broadcast/" + LONDON + "?date=" + LocalDate.now())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

    @Test
    public void getCityByDayNextAbsentTest() throws Exception {
        List<BroadcastCity> stubCities = Arrays.asList(
                TestUtils.getStub("london-11-01", BroadcastCity[].class));
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>(stubCities);
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withSelfRel()
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now()));
        expectedResult.add(linkTo(methodOn(BroadcastController.class)
                .getCityByDay(LONDON, LocalDate.now().minusDays(1)))
                .withRel("prevDay")
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now().minusDays(1)));

        when(broadcastService.find(LONDON.toLowerCase(), LocalDate.now())).thenReturn(stubCities);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().plusDays(1))).thenReturn(false);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().minusDays(1))).thenReturn(true);

        mockMvc.perform(get("/broadcast/" + LONDON + "?date=" + LocalDate.now())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

    @Test
    public void getCityByDayPrevAbsentTest() throws Exception {
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>(new ArrayList<>());
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withSelfRel()
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now()));
        expectedResult.add(linkTo(methodOn(BroadcastController.class)
                .getCityByDay(LONDON, LocalDate.now().plusDays(1)))
                .withRel("nextDay")
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now().plusDays(1)));

        when(broadcastService.find(LONDON.toLowerCase(), LocalDate.now())).thenReturn(new ArrayList<>());
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().plusDays(1))).thenReturn(true);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().minusDays(1))).thenReturn(false);

        mockMvc.perform(get("/broadcast/" + LONDON + "?date=" + LocalDate.now())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

    @Test
    public void getCityByDayPrevAndNextAbsentTest() throws Exception {
        List<BroadcastCity> stubCities = Arrays.asList(
                TestUtils.getStub("london-11-01", BroadcastCity[].class));
        HateaosResource<List<BroadcastCity>> expectedResult = new HateaosResource<>(stubCities);
        expectedResult.add(linkTo(methodOn(BroadcastController.class).getCityByDay(LONDON, LocalDate.now()))
                .withSelfRel()
                .withHref("http://localhost/broadcast/london?date=" + LocalDate.now()));

        when(broadcastService.find(LONDON.toLowerCase(), LocalDate.now())).thenReturn(stubCities);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().plusDays(1))).thenReturn(false);
        when(broadcastService.exists(LONDON.toLowerCase(), LocalDate.now().minusDays(1))).thenReturn(false);

        mockMvc.perform(get("/broadcast/" + LONDON + "?date=" + LocalDate.now())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(TestUtils.formJsonString(expectedResult)));
    }

}