package com.uddernetworks.modelcreator.main;

import com.google.gson.GsonBuilder;
import com.uddernetworks.modelcreator.json.*;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }

    public static void main(String[] args) {
        try {
            MappedTextures mappedTextures = new MappedTextures();

            File schematicFile = new File("E:\\ModelCreator\\schematics\\test_schematic.schematic");
            File outputFile = new File("E:\\ModelCreator\\json\\output.json");


            FileInputStream fis = new FileInputStream(schematicFile);
            NBTInputStream nbt = new NBTInputStream(fis);
            CompoundTag backuptag = (CompoundTag) nbt.readTag();
            Map<String, Tag> tagCollection = backuptag.getValue();

            short width = (Short)getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
            short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();

            byte[] blockBytes = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            byte[] dataBytes = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

            List entities = (List) getChildTag(tagCollection, "Entities", ListTag.class).getValue();
            List tileentities = (List) getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();
            nbt.close();
            fis.close();

            List<Block> blocks = getBlocksFromData(blockBytes, dataBytes);

            Blocks3D blocks3D = new Blocks3D(mappedTextures, blocks, length, width, height);

            String file = new String(Files.readAllBytes(Paths.get("E:\\ModelCreator\\json\\template.json")));

            Model model = new GsonBuilder().create().fromJson(file, Model.class);

            List<Element> elements = new ArrayList<>();

            List<Double> uv = Arrays.asList(0D, 0D, 0D, 0D);

            blocks3D.iterate(mappedTextures, (block, coordinateSet) -> {
                String texture = block.getTexture(mappedTextures);

                Faces faces = new Faces(
                        new North(texture, uv),
                        new East(texture, uv),
                        new South(texture, uv),
                        new West(texture, uv),
                        new Up(texture, uv),
                        new Down(texture, uv));

                elements.add(new Element(coordinateSet.getFirst(), coordinateSet.getSecond(), faces));
            });

            System.out.println("Done");

            model.setElements(elements);

            String gson = new GsonBuilder().setPrettyPrinting().create().toJson(model);

            Files.write(outputFile.toPath(), gson.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Block> getBlocksFromData(byte[] blocks, byte[] data) {
        List<Block> ret = new ArrayList<>();

        for (int i = 0; i < blocks.length; i++) {
            ret.add(new Block(blocks[i], data[i]));
        }

        return ret;
    }
}
