
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Main {

    @SerializedName("temp")
    @Expose
    public Double temp;
    @SerializedName("temp_min")
    @Expose
    public Double tempMin;
    @SerializedName("temp_max")
    @Expose
    public Double tempMax;
    @SerializedName("pressure")
    @Expose
    public Double pressure;
    @SerializedName("sea_level")
    @Expose
    public Double seaLevel;
    @SerializedName("grnd_level")
    @Expose
    public Double grndLevel;
    @SerializedName("humidity")
    @Expose
    public Integer humidity;
    @SerializedName("temp_kf")
    @Expose
    public Double tempKf;

}
