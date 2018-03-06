
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Coordinates {

    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("lon")
    @Expose
    private Double longitude;

}
