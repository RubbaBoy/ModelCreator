
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class West {

    @SerializedName("texture")
    @Expose
    private String texture;
    @SerializedName("uv")
    @Expose
    private List<Double> uv = null;

    public West(String texture, List<Double> uv) {
        this.texture = texture;
        this.uv = uv;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public List<Double> getUv() {
        return uv;
    }

    public void setUv(List<Double> uv) {
        this.uv = uv;
    }

}
