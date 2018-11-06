package com.tramonti.weather.domain.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class WeatherException extends RuntimeException {

    private HttpStatus status;
    private Throwable throwable;
    private String name;
    private String description;
    private Level level;
    private String className;

    public WeatherException setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public WeatherException setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public WeatherException setName(String name) {
        this.name = name;
        return this;
    }

    public WeatherException setDescription(String description) {
        this.description = description;
        return this;
    }

    public WeatherException setLevel(Level level) {
        this.level = level;
        return this;
    }

    public WeatherException setClassName(String className){
        this.className = className;
        return this;
    }

    public enum Level {
        FATAL, ERROR, WARNING, INFO
    }

}
