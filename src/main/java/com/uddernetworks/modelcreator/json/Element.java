
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Element {

    @SerializedName("from")
    @Expose
    private List<Integer> from = null;
    @SerializedName("to")
    @Expose
    private List<Integer> to = null;
    @SerializedName("faces")
    @Expose
    private Faces faces;

    public Element(List<Integer> from, List<Integer> to, Faces faces) {
        this.from = from;
        this.to = to;
        this.faces = faces;
    }

    public List<Integer> getFrom() {
        return from;
    }

    public void setFrom(List<Integer> from) {
        this.from = from;
    }

    public List<Integer> getTo() {
        return to;
    }

    public void setTo(List<Integer> to) {
        this.to = to;
    }

    public Faces getFaces() {
        return faces;
    }

    public void setFaces(Faces faces) {
        this.faces = faces;
    }
}
