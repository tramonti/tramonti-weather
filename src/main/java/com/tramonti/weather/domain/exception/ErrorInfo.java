package com.tramonti.weather.domain.exception;

import lombok.Data;

@Data
public class ErrorInfo {
    String status;
    String name;
    String description;
}
