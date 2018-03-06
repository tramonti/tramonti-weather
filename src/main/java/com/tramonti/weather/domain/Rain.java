
package com.tramonti.weather.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Rain {

    @SerializedName("3h")
    @Expose
    public Double _3h;

}
