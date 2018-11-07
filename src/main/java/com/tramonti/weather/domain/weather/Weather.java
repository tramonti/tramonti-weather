package com.tramonti.weather.domain.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class Weather {

    private Integer id;
    private String main;
    private String description;
    private String icon;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final Weather other = (Weather) obj;

        return Objects.equals(this.id, other.id)
                && Objects.equals(this.main, other.main)
                && Objects.equals(this.description, other.description)
                && Objects.equals(this.icon, other.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, main, description, icon);
    }

}
