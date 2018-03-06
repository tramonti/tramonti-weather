
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Coord {

    @SerializedName("lat")
    @Expose
    public Float lat;
    @SerializedName("lon")
    @Expose
    public Float lon;

}
