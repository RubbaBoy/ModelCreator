
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Head {

    @SerializedName("rotation")
    @Expose
    private List<Double> rotation = null;
    @SerializedName("translation")
    @Expose
    private List<Double> translation = null;
    @SerializedName("scale")
    @Expose
    private List<Double> scale = null;

    public List<Double> getRotation() {
        return rotation;
    }

    public void setRotation(List<Double> rotation) {
        this.rotation = rotation;
    }

    public List<Double> getTranslation() {
        return translation;
    }

    public void setTranslation(List<Double> translation) {
        this.translation = translation;
    }

    public List<Double> getScale() {
        return scale;
    }

    public void setScale(List<Double> scale) {
        this.scale = scale;
    }

}
