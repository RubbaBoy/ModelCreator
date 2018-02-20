
package com.uddernetworks.modelcreator.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Faces {

    @SerializedName("North")
    @Expose
    private North north;
    @SerializedName("East")
    @Expose
    private East east;
    @SerializedName("South")
    @Expose
    private South south;
    @SerializedName("West")
    @Expose
    private West west;
    @SerializedName("Up")
    @Expose
    private Up up;
    @SerializedName("Down")
    @Expose
    private Down down;

    public Faces(North north, East east, South south, West west, Up up, Down down) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public North getNorth() {
        return north;
    }

    public void setNorth(North north) {
        this.north = north;
    }

    public East getEast() {
        return east;
    }

    public void setEast(East east) {
        this.east = east;
    }

    public South getSouth() {
        return south;
    }

    public void setSouth(South south) {
        this.south = south;
    }

    public West getWest() {
        return west;
    }

    public void setWest(West west) {
        this.west = west;
    }

    public Up getUp() {
        return up;
    }

    public void setUp(Up up) {
        this.up = up;
    }

    public Down getDown() {
        return down;
    }

    public void setDown(Down down) {
        this.down = down;
    }

}
