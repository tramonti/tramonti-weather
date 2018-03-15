package com.tramonti.weather.domain;

import lombok.Data;

@Data
public class ErrorInfo {
    String status;
    String name;
    String description;
}
