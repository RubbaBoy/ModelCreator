package com.uddernetworks.modelcreator.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class Blocks3D {

    private List<BlockPlane> blocks = new ArrayList<>();

    public Blocks3D(MappedTextures mappedTextures, List<Block> blocks, int length, int width, int height) {
        int section = length * width;

        for (int i = 0; i < height; i++) {
            int offset = i * section;

            BlockPlane blockPlane = new BlockPlane(length, width);

            for (int i2 = 0; i2 < section; i2++) {
                int index = offset + i2;

                int x = i2 % 16;
                int z = i2 / 16;

//                System.out.println("(" + x + ", " + z + ")");

//                if (blocks.get(index).getTexture(mappedTextures) == null || blocks.get(index).getTexture(mappedTextures).equals("#transparent")) continue;

                blockPlane.setBlock(blocks.get(index), x, z);
            }

            this.blocks.add(blockPlane);
        }
    }

    public void iterate(MappedTextures mappedTextures, BiConsumer<Block, CoordinateSet> consumer) throws CloneNotSupportedException {
        for (int y = 0; y < blocks.size(); y++) {
            int finalY = y;
            blocks.get(y).loop(mappedTextures, (block, x, z) -> {
                if (block.isEnabled()) {
                    CoordinateSet coordinateSet = fillBiggest(mappedTextures, block, new CoordinateSet(x, finalY, z, x + 1, finalY + 1, z + 1));
                    consumer.accept(block, coordinateSet);
                }
            });
        }
    }

    public CoordinateSet fillBiggest(MappedTextures mappedTextures, Block block, CoordinateSet coordinateSet) throws CloneNotSupportedException {
        int startX = coordinateSet.getX1();
        int startY = coordinateSet.getY1();
        int startZ = coordinateSet.getZ1();

        String startTexture = block.getTexture(mappedTextures);

        if (startTexture.equals("#transparent")) return coordinateSet;

        /*
         * START OF XXXXXXXXXXXXXXXXXXXXXX VALUES
         */

        CoordinateSet xCoordinates = coordinateSet.clone();
        List<Block> xBlocks = new ArrayList<>();

        CoordinateSet yCoordinates = coordinateSet.clone();
        List<Block> yBlocks = new ArrayList<>();

        CoordinateSet zCoordinates = coordinateSet.clone();
        List<Block> zBlocks = new ArrayList<>();

        // Going through future positive values on X axis

        int index = 0;

        while (true) {
            index++;

            Block nextBlock = getBlock(startX + index, startY, startZ);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            xCoordinates.setX2(startX + index);
            xBlocks.add(nextBlock);
        }

        // Going through all future negative values on X axis

        index = 0;

        while (true) {
            index--;

            Block nextBlock = getBlock(startX + index, startY, startZ);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            xCoordinates.setX1(startX + index);
            xBlocks.add(nextBlock);
        }

        /*
         * END OF XXXXXXXXXXXXXXXXXXXXXX VALUES
         */



        /*
         * START OF YYYYYYYYYYYYYYYYYYYYYYY VALUES
         */

        // Going through future positive values on Y axis

        index = 0;

        while (true) {
            index++;

            Block nextBlock = getBlock(startX, startY + index, startZ);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            yCoordinates.setY2(startY + index);
            yBlocks.add(nextBlock);
        }

        // Going through all future negative values on Y axis

        index = 0;

        while (true) {
            index--;

            Block nextBlock = getBlock(startX, startY + index, startZ);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            yCoordinates.setY1(startY + index);
            yBlocks.add(nextBlock);
        }

        /*
         * END OF YYYYYYYYYYYYYYYYYYYYYYY VALUES
         */



        /*
         * START OF ZZZZZZZZZZZZZZZZZZZZZZZZZ VALUES
         */

        // Going through future positive values on Z axis

        index = 0;

        while (true) {
            index++;

            Block nextBlock = getBlock(startX, startY, startZ + index);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            zCoordinates.setZ2(startZ + index);
            zBlocks.add(nextBlock);
        }

        // Going through all future negative values on Z axis

        index = 0;

        while (true) {
            index--;

            Block nextBlock = getBlock(startX, startY, startZ + index);

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            zCoordinates.setZ1(startZ + index);
            zBlocks.add(nextBlock);
        }

        /*
         * END OF ZZZZZZZZZZZZZZZZZZZZZZZZZ VALUES
         */


        int direction = 0; // 0 = x
                           // 1 = y
                           // 2 = z

        if (yBlocks.size() > xBlocks.size()) {
            direction = 1;
        }

        if (direction == 1) {
            if (zBlocks.size() > yBlocks.size()) {
                direction = 2;
            }
        } else {
            if (zBlocks.size() > xBlocks.size()) {
                direction = 2;
            }
        }

        System.out.println("direction = " + direction);

        switch (direction) {
            case 0:
                xBlocks.forEach(xBlock -> xBlock.setEnabled(false));
                return xCoordinates;
            case 1:
                yBlocks.forEach(yBlock -> yBlock.setEnabled(false));
                return yCoordinates;
            case 2:
                zBlocks.forEach(zBlock -> zBlock.setEnabled(false));
                return zCoordinates;
        }

        return coordinateSet;
    }

    public void disableBlock(int x, int y, int z) {
        this.blocks.get(y).getBlock(x, z).setEnabled(false);
    }

    public Block getBlock(int x, int y, int z) {
        if (y >= this.blocks.size() || y < 0) return null;
        return this.blocks.get(y).getBlock(x, z);
    }

    public class CoordinateSet implements Cloneable {
        private int x1;
        private int y1;
        private int z1;

        private int x2;
        private int y2;
        private int z2;

        public CoordinateSet(int x1, int y1, int z1, int x2, int y2, int z2) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
        }

        public List<Integer> getFirst() {
            return Arrays.asList(x1, y1, z1);
        }

        public List<Integer> getSecond() {
            return Arrays.asList(x2, y2, z2);
        }

        public int getX1() {
            return x1;
        }

        public void setX1(int x1) {
            this.x1 = x1;
        }

        public int getY1() {
            return y1;
        }

        public void setY1(int y1) {
            this.y1 = y1;
        }

        public int getZ1() {
            return z1;
        }

        public void setZ1(int z1) {
            this.z1 = z1;
        }

        public int getX2() {
            return x2;
        }

        public void setX2(int x2) {
            this.x2 = x2;
        }

        public int getY2() {
            return y2;
        }

        public void setY2(int y2) {
            this.y2 = y2;
        }

        public int getZ2() {
            return z2;
        }

        public void setZ2(int z2) {
            this.z2 = z2;
        }

        @Override
        protected CoordinateSet clone() throws CloneNotSupportedException {
            return new CoordinateSet(x1, y1, z1, x2, y2, z2);
        }
    }

}
