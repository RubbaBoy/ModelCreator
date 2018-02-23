package com.uddernetworks.modelcreator.main;

import com.google.gson.GsonBuilder;
import com.uddernetworks.modelcreator.json.*;
import org.jnbt.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static Tag getChildTag(Map<String, Tag> items, String key) {
        return items.get(key);
    }

    public static void main(String[] args) {
        try {
            MappedTextures mappedTextures = new MappedTextures();

            long start = System.currentTimeMillis();

            if (args.length != 2) {
                System.err.println("Invalid format. Arguments should be schematic file first, output file second, and an optional true/false value to display layers in console.");
                System.exit(0);
            }

            File schematicFile = new File(args[0]);
            File outputFile = new File(args[1]);
            outputFile.createNewFile();

            FileInputStream fis = new FileInputStream(schematicFile);
            NBTInputStream nbt = new NBTInputStream(fis);
            CompoundTag backuptag = (CompoundTag) nbt.readTag();
            Map<String, Tag> tagCollection = backuptag.getValue();

            short width = (Short) getChildTag(tagCollection, "Width").getValue();
            short height = (Short) getChildTag(tagCollection, "Height").getValue();
            short length = (Short) getChildTag(tagCollection, "Length").getValue();

            byte[] blockBytes = (byte[]) getChildTag(tagCollection, "Blocks").getValue();
            byte[] dataBytes = (byte[]) getChildTag(tagCollection, "Data").getValue();

            List entities = (List) getChildTag(tagCollection, "Entities").getValue();
            List tileentities = (List) getChildTag(tagCollection, "TileEntities").getValue();
            nbt.close();
            fis.close();

            List<Block> blocks = getBlocksFromData(blockBytes, dataBytes);

            Blocks3D blocks3D = new Blocks3D(mappedTextures, blocks, length, width, height);

            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("template.json");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[1024];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();

            String text = new String(buffer.toByteArray(), StandardCharsets.UTF_8);

            Model model = new GsonBuilder().create().fromJson(text, Model.class);

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

            model.setElements(elements);

            String gson = new GsonBuilder().setPrettyPrinting().create().toJson(model);

            Files.write(outputFile.toPath(), gson.getBytes());

            System.out.println("Done in " + (System.currentTimeMillis() - start) + "ms");

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
