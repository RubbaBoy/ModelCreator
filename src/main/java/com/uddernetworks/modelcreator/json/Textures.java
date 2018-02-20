
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Textures implements Cloneable {

    @SerializedName("1")
    @Expose
    private String _1;

    public String get1() {
        return _1;
    }

    public void set1(String _1) {
        this._1 = _1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Textures textures = new Textures();
        textures._1 = this._1;
        return textures;
    }
}
