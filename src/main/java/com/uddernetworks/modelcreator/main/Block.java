package com.uddernetworks.modelcreator.main;

import com.uddernetworks.modelcreator.json.Textures;

public class Block {
    private int id;
    private int data;
    private boolean enabled = true;

    public Block(int id, int data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getData() {
        return data;
    }

    @Override
    public String toString() {
        return "[" + id + ", " + data + "]";
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTexture(MappedTextures mappedTextures) {
        return "#" + mappedTextures.getTexture(this.id, this.data);
    }
}
