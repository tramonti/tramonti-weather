package com.tramonti.weather.repository;

import com.google.gson.Gson;
import com.tramonti.weather.domain.exception.CityNotFoundException;
import com.tramonti.weather.domain.weather.OpenWeather;
import lombok.Cleanup;
import org.apache.log4j.NDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

//FIXME: calling 3rd party service is not repository

@Repository
public class WeatherRepositoryImpl implements WeatherRepository {



    //@Autowired
   // private RestTemplate restTemplate;

    //FIXME: Url string should be in properties file
    // @Value("${weatherUrl}")
     //private String weatherUrl;

    //in format acceptable to RestTemplate

    @Override
    public OpenWeather getWeather(String cityName) {
        String urlString = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&units=metric&APPID=713d9a6a9e5940714f0f60d0f56a095d";
        OpenWeather openWeather;
        try {

            //TODO: refactor to RestTemplate


            //restTemplate. getForObject(urlString, OpenWeather.class);

            URL url = new URL(urlString);
            @Cleanup
            InputStreamReader reader = new InputStreamReader(url.openStream());
            openWeather = new Gson().fromJson(reader, OpenWeather.class);
        } catch (IOException e) {
            NDC.push("cityName = " + cityName);
            NDC.push("exception = " + e.toString());
            throw new CityNotFoundException()
                    .setDescription("city not found")
                    .setName("Illegal City Name")
                    .setLevel(CityNotFoundException.Level.ERROR)
                    .setStatus(HttpStatus.NOT_FOUND);
        }
        return openWeather;
    }

//    URI targetUrl = UriComponentsBuilder
//            .fromHttpUrl(this.aviosRegisterServiceUrl.replace(MEMBERSHIP_ID_URI, membershipId))
//            .queryParam("api_key", aviosApiKey).build().toUri();
//
//    HttpEntity<Object> requestEntity = new HttpEntity<Object>(registerRQ, HttpAviosHeadersUtils.withContent());
//    ResponseEntity<AviosRegisterRS> responseEntity = null;
//		try {
//        log.info("Sending avios register request: {}", requestEntity);
//        responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST, requestEntity, AviosRegisterRS.class);
//        if (responseEntity != null && !responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
//            throw new AviosRegistrationFailedException("Avios registration failed");
//        } else {
//            response = responseEntity.getBody();
//        }
//    } catch (HttpClientErrorException e) {
//        log.warn("Failure calling avios register", e);
//        throw e;
//    }




}
