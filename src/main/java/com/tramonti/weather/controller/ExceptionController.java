package com.tramonti.weather.controller;

import com.tramonti.weather.domain.ErrorInfo;
import com.tramonti.weather.domain.WeatherException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ExceptionController {

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ErrorInfo> handleWeatherException(WeatherException e) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setName(e.getName());
        errorInfo.setDescription(e.getDescription());
        errorInfo.setStatus(e.getStatus().toString());
        return new ResponseEntity<>(errorInfo, e.getStatus());
    }

}
