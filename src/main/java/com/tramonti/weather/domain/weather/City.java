
package com.tramonti.weather.domain.weather;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

//TODO: refactor equals, hash, toString using Java8 Objects
//or Guava or Apache Commons
@Getter
@Setter
public class City {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("coord")
    @Expose
    private Coordinates coordinates;
    @SerializedName("country")
    @Expose
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
