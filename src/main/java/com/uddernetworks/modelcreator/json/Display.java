
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Display implements Cloneable {

    @SerializedName("head")
    @Expose
    private Head head;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Display display = new Display();
        display.head = this.head;
        return display;
    }
}
