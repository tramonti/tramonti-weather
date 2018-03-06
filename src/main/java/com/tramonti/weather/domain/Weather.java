
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class Weather {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("main")
    @Expose
    private String main;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("icon")
    @Expose
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
