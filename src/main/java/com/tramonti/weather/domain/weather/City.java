package com.tramonti.weather.domain.weather;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    private Integer id;
    private String name;
    @JsonAlias("coord")
    private Coordinates coordinates;
    private String country;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("coordinates", coordinates)
                .add("country", country)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.name, this.country, this.coordinates);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final City other = (City) obj;
        return Objects.equal(this.id, other.id)
                && Objects.equal(this.name, other.name)
                && Objects.equal(this.country, other.country)
                && Objects.equal(this.coordinates, other.coordinates);
    }
}
