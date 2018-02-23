
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Model implements Cloneable {

    @SerializedName("__comment")
    @Expose
    private String comment;
    @SerializedName("textures")
    @Expose
    private Object textures;
    @SerializedName("display")
    @Expose
    private Display display;
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTextures(Textures textures) {
        this.textures = textures;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Model model = new Model();
        model.comment = this.comment;
        model.textures = this.textures;
        model.display = this.display;
        model.elements = new ArrayList<>(this.elements);
        return model;
    }
}
