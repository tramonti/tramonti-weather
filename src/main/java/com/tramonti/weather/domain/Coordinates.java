
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter @Setter
public class Coordinates {

    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("lon")
    @Expose
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
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
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
