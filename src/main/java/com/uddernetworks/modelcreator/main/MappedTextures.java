package com.uddernetworks.modelcreator.main;

import com.uddernetworks.modelcreator.json.Textures;

import java.util.HashMap;
import java.util.Map;

public class MappedTextures {

    private Map<Data, Texture> textures = new HashMap<>();

    public MappedTextures() {
        textures.put(Data.from(0, 0), Texture.TRANSPARENT);
        textures.putAll(allFrom(35)); // Wool
        textures.putAll(allFrom(95)); // Glass
        textures.putAll(allFrom(159)); // Hardened Clay
        textures.putAll(allFrom(160)); // Glass Pane
        textures.putAll(allFrom(251)); // Concrete
        textures.putAll(allFrom(252)); // Concrete Powder
    }

    public String getTexture(int id, int data) {
        Texture texture = textures.get(Data.from(id, data));
        return texture != null ? texture.getName() : "unknown";
    }

    private Map<Data, Texture> allFrom(int id) {
        Map<Data, Texture> ret = new HashMap<>();

        for (int i = 0; i < Texture.values().length; i++) {
            ret.put(Data.from(id, i), Texture.values()[i]);
        }

        return ret;
    }

    static class Data {
        private int id;
        private int data;

        public Data(int id, int data) {
            this.id = id;
            this.data = data;
        }

        public static Data from(int id, int data) {
            return new Data(id, data);
        }

        public int getId() {
            return id;
        }

        public int getData() {
            return data;
        }

        @Override
        public int hashCode() {
            return (1000 * id) + data;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Data)) return false;
            Data dataObject = (Data) object;
            if (dataObject == this) return true;

            return dataObject.id == this.id && dataObject.data == this.data;
        }

        @Override
        public String toString() {
            return id + ":" + data;
        }
    }

}
