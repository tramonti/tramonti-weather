package com.tramonti.weather.domain.weather;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
public class Coordinates {

    @JsonAlias("lat")
    private Double latitude;
    @JsonAlias("lon")
    private Double longitude;

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Coordinates other = (Coordinates) obj;
        return new EqualsBuilder()
                .append(this.latitude, other.latitude)
                .append(this.latitude, other.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(longitude)
                .append(latitude)
                .toHashCode();

    }
}
