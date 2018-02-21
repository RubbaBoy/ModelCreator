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

                blockPlane.setBlock(blocks.get(index), x, z);
            }

            this.blocks.add(blockPlane);
        }
    }

    public void iterate(MappedTextures mappedTextures, BiConsumer<Block, CoordinateSet> consumer) throws CloneNotSupportedException {
        printLaters();
        for (int y = 0; y < blocks.size(); y++) {
            int finalY = y;
            blocks.get(y).loop(mappedTextures, (block, x, z) -> {
                if (block.isEnabled()) {
                    CoordinateSet coordinateSet = fillBiggest(mappedTextures, block, new CoordinateSet(x, finalY, z, x + 1, finalY + 1, z + 1));
                    printLaters();
                    consumer.accept(block, coordinateSet);
                }
            });
        }
    }

    private void printLaters() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < blocks.size(); i++) {
            stringBuilder.append("＝＝＝＝＝ Layer ").append(i).append(" ＝＝＝＝＝\n");
            stringBuilder.append(blocks.get(i).toString()).append("\n");
        }

        stringBuilder.append("\n");

        System.out.println(stringBuilder.toString());
    }

    private String repeat(String repeat, int amount) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            stringBuilder.append(repeat);
        }

        return stringBuilder.toString();
    }

    public CoordinateSet fillBiggest(MappedTextures mappedTextures, Block block, CoordinateSet coordinateSet) throws CloneNotSupportedException {
        int startX = coordinateSet.getX1();
        int startY = coordinateSet.getY1();
        int startZ = coordinateSet.getZ1();

        String startTexture = block.getTexture(mappedTextures);

        if (startTexture.equals("#transparent")) return coordinateSet;

        CoordinateSet xCoordinates = coordinateSet.clone();
        List<Block> xBlocks = new ArrayList<>();
        List<List<Integer>> xBlockCoordinates = new ArrayList<>();

        CoordinateSet yCoordinates = coordinateSet.clone();
        List<Block> yBlocks = new ArrayList<>();
        List<List<Integer>> yBlockCoordinates = new ArrayList<>();

        CoordinateSet zCoordinates = coordinateSet.clone();
        List<Block> zBlocks = new ArrayList<>();
        List<List<Integer>> zBlockCoordinates = new ArrayList<>();

        xBlocks.addAll(shiftSingle(mappedTextures, startTexture, xCoordinates, xBlockCoordinates, startX, startY, startZ, 1, 0, 0, true));
        xBlocks.addAll(shiftSingle(mappedTextures, startTexture, xCoordinates, xBlockCoordinates, startX, startY, startZ, -1, 0, 0, false));

        yBlocks.addAll(shiftSingle(mappedTextures, startTexture, yCoordinates, yBlockCoordinates, startX, startY, startZ, 0, 1, 0, true));
        yBlocks.addAll(shiftSingle(mappedTextures, startTexture, yCoordinates, yBlockCoordinates, startX, startY, startZ, 0, -1, 0, false));

        zBlocks.addAll(shiftSingle(mappedTextures, startTexture, zCoordinates, zBlockCoordinates, startX, startY, startZ, 0, 0, 1, true));
        zBlocks.addAll(shiftSingle(mappedTextures, startTexture, zCoordinates, zBlockCoordinates, startX, startY, startZ, 0, 0, -1, false));

        int direction = 0; // 0 = x
                           // 1 = y
                           // 2 = z

        if (zBlocks.size() > xBlocks.size()) {
            direction = 1;
        }

        if (direction == 1) {
            if (yBlocks.size() > zBlocks.size()) {
                direction = 2;
            }
        } else {
            if (yBlocks.size() > xBlocks.size()) {
                direction = 2;
            }
        }

        switch (direction) {
            case 0:
                CoordinateSet firstTemp = xCoordinates.clone();
                List<Block> fullYBlocks = new ArrayList<>(xBlocks);
                fullYBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, xBlockCoordinates, 0, 1, 0));
                fullYBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, xBlockCoordinates, 0, -1, 0));

                CoordinateSet secondTemp = xCoordinates.clone();

                List<Block> fullZBlocks = new ArrayList<>(xBlocks);
                fullZBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, xBlockCoordinates, 0, 0, 1));
                fullZBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, xBlockCoordinates, 0, 0, -1));

                if (fullYBlocks.size() > fullZBlocks.size()) {
                    fullYBlocks.forEach(yBlock -> yBlock.setEnabled(false));
                    return firstTemp;
                } else {
                    fullZBlocks.forEach(zBlock -> zBlock.setEnabled(false));
                    return secondTemp;
                }
            case 1:
                firstTemp = yCoordinates.clone();
                List<Block> fullXBlocks = new ArrayList<>(yBlocks);
                fullXBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, yBlockCoordinates, 1, 0, 0));
                fullXBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, yBlockCoordinates, -1, 0, 0));

                secondTemp = yCoordinates.clone();
                fullZBlocks = new ArrayList<>(yBlocks);
                fullZBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, yBlockCoordinates, 0, 0, 1));
                fullZBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, yBlockCoordinates, 0, 0, -1));

                if (fullZBlocks.size() > fullXBlocks.size()) {
                    fullZBlocks.forEach(zBlock -> zBlock.setEnabled(false));
                    return secondTemp;
                } else {
                    fullXBlocks.forEach(xBlock -> xBlock.setEnabled(false));
                    return firstTemp;
                }
            case 2:
                firstTemp = zCoordinates.clone();
                fullXBlocks = new ArrayList<>(zBlocks);
                fullXBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, zBlockCoordinates, 1, 0, 0));
                fullXBlocks.addAll(shiftRow(mappedTextures, startTexture, firstTemp, zBlockCoordinates, -1, 0, 0));


                secondTemp = zCoordinates.clone();
                fullYBlocks = new ArrayList<>(zBlocks);
                fullYBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, zBlockCoordinates, 0, 1, 0));
                fullYBlocks.addAll(shiftRow(mappedTextures, startTexture, secondTemp, zBlockCoordinates, 0, -1, 0));

                if (fullYBlocks.size() > fullXBlocks.size()) {
                    fullYBlocks.forEach(yBlock -> yBlock.setEnabled(false));
                    return secondTemp;
                } else {
                    fullXBlocks.forEach(xBlock -> xBlock.setEnabled(false));
                    return firstTemp;
                }
        }

        return coordinateSet;
    }

    private List<Block> shiftSingle(MappedTextures mappedTextures, String startTexture, CoordinateSet coordinateSet, List<List<Integer>> blockCoordinates, int startX, int startY, int startZ, int x, int y, int z, boolean addFirst) {
        List<Block> ret = new ArrayList<>();
        int index = 0;

        while (true) {
            Block nextBlock = getBlock(startX + (x != 0 ? index : 0), startY + (y != 0 ? index : 0), startZ + (z != 0 ? index : 0));

            if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) break;

            if (addFirst) {
                blockCoordinates.add(Arrays.asList(startX + (x != 0 ? index : 0), startY + (y != 0 ? index : 0), startZ + (z != 0 ? index : 0)));
                ret.add(nextBlock);
            }

            if (x > 0) {
                coordinateSet.setX2(startX + index);
            } else if (x < 0) {
                coordinateSet.setX1(startX + index);
            }

            if (y > 0) {
                coordinateSet.setY2(startY + index);
            } else if (y < 0) {
                coordinateSet.setY1(startY + index);
            }

            if (z > 0) {
                coordinateSet.setZ2(startZ + index);
            } else if (z < 0) {
                coordinateSet.setZ1(startZ + index);
            }

            index += (x < 0 || y < 0 || z < 0) ? -1 : 1;
        }

        return ret;
    }

    private List<Block> shiftRow(MappedTextures mappedTextures, String startTexture, CoordinateSet coordinateSet, List<List<Integer>> startCoords, int x, int y, int z) throws CloneNotSupportedException {
        List<Block> ret = new ArrayList<>();
        int index = 0;

        CoordinateSet originalCoordinateSet = coordinateSet.clone();

        while (true) {
            List<Block> nextBlocks = new ArrayList<>();

            for (List<Integer> startCoord : startCoords) {
                nextBlocks.add(getBlock(startCoord.get(0) + (x != 0 ? index : 0), startCoord.get(1) + (y != 0 ? index : 0), startCoord.get(2) + (z != 0 ? index : 0)));
            }

            if (nextBlocks.size() == 0) return ret;

            for (Block nextBlock : nextBlocks) {
                if (nextBlock == null || !nextBlock.getTexture(mappedTextures).equals(startTexture) || !nextBlock.isEnabled()) return ret;
            }

            ret.addAll(nextBlocks);

            if (x > 0) {
                coordinateSet.setX2(originalCoordinateSet.getX2() + index);
            } else if (x < 0) {
                coordinateSet.setX1(originalCoordinateSet.getX1() + index);
            }

            if (y > 0) {
                coordinateSet.setY2(originalCoordinateSet.getY2() + index);
            } else if (y < 0) {
                coordinateSet.setY1(originalCoordinateSet.getY1() + index);
            }

            if (z > 0) {
                coordinateSet.setZ2(originalCoordinateSet.getZ2() + index);
            } else if (z < 0) {
                coordinateSet.setZ1(originalCoordinateSet.getZ1() + index);
            }

            index += (x < 0 || y < 0 || z < 0) ? -1 : 1;
        }
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
        public String toString() {
            return "[(" + x1 + ", " + y1 + ", " + z1 + "), (" + x2 + ", " + y2 + ", " + z2 + ")]";
        }

        @Override
        protected CoordinateSet clone() {
            return new CoordinateSet(x1, y1, z1, x2, y2, z2);
        }
    }

}
