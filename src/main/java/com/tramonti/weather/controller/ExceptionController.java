package com.tramonti.weather.controller;

import com.tramonti.weather.domain.ErrorInfo;
import com.tramonti.weather.domain.WeatherException;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j
public class ExceptionController {
    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ErrorInfo> handleWeatherException(WeatherException e) {
        switch (e.getLevel()) {
            case FATAL:
                log.fatal(e.getDescription(), e.getThrowable());
                break;
            case ERROR:
                log.error(e.getDescription(), e.getThrowable());
                break;
            case WARNING:
                log.warn(e.getDescription(), e.getThrowable());
                break;
            case INFO:
                log.info(e.getDescription(), e.getThrowable());
                break;
        }

        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setName(e.getName());
        errorInfo.setDescription(e.getDescription());
        errorInfo.setStatus(e.getStatus().toString());
        return new ResponseEntity<>(errorInfo, e.getStatus());
    }

}
