package com.tramonti.weather.controller;

import com.tramonti.weather.domain.exception.ErrorInfo;
import com.tramonti.weather.domain.exception.WeatherException;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.NDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j
public class ExceptionController {
    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ErrorInfo> handleWeatherException(WeatherException e) {
        log(e);

        ResponseEntity<ErrorInfo> errorResponse = buildResponse(e);
        return errorResponse;
    }

    private void log(WeatherException e) {
        String info = String.format("Class: [%s], description[%s]", e.getClassName(), e.getDescription());
        switch (e.getLevel()) {
            case FATAL:
                log.fatal(info, e.getThrowable());
                break;
            case ERROR:
                log.error(info, e.getThrowable());
                break;
            case WARNING:
                log.warn(info, e.getThrowable());
                break;
            case INFO:
                log.info(info, e.getThrowable());
                break;
        }
        clearNDC();
    }

    private void clearNDC() {
        for (int i = 0, NDCDepth = NDC.getDepth(); i < NDCDepth; i++) {
            NDC.pop();
        }
    }

    private ResponseEntity<ErrorInfo> buildResponse(WeatherException e) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setName(e.getName());
        errorInfo.setDescription(e.getDescription());
        errorInfo.setStatus(e.getStatus().toString());
        return new ResponseEntity<>(errorInfo, e.getStatus());
    }
}
