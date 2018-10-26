package com.tramonti.weather.domain.broadcast;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "broadcast")
@Getter
@EqualsAndHashCode
public class BroadcastCity {

    @Id
    private String id;
    private String city;
    private Double temperature;
    private String description;
    private String icon;
    private LocalDateTime dateTime;

    public BroadcastCity setId(String id) {
        this.id = id;
        return this;
    }

    public BroadcastCity setCity(String city) {
        this.city = city;
        return this;
    }

    public BroadcastCity setTemperature(Double temperature) {
        this.temperature = temperature;
        return this;
    }

    public BroadcastCity setDescription(String description) {
        this.description = description;
        return this;
    }

    public BroadcastCity setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public BroadcastCity setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }
}
