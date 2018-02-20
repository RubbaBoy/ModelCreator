package com.uddernetworks.modelcreator.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BlockPlane {

    private Block[][] grid;

    public BlockPlane(int length, int width) {
        grid = new Block[width][length];
    }

    /**
     * Sets a block at the specified position
     * @param x The X coordinate of the block (The position in the row, the inner array)
     * @param z The Z coordinate of the block (What row it is in, the outer array)
     */
    public void setBlock(Block block, int x, int z) {
        grid[z][x] = block;
    }

    /**
     * Gets the specific block at the given coordinates
     * @param x The X coordinate of the block (The position in the row, the inner array)
     * @param z The Z coordinate of the block (What row it is in, the outer array)
     * @return The requested block
     */
    public Block getBlock(int x, int z) {
        if (x >= grid.length || z >= grid[0].length ||
            x < 0 || z < 0) return null;
        return grid[z][x].isEnabled() ? grid[z][x] : null;
    }

    public void loop(MappedTextures mappedTextures, TriConsumer<Block, Integer, Integer> triConsumer) throws CloneNotSupportedException {
        for (int z = 0; z < grid.length; z++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (!grid[z][x].getTexture(mappedTextures).equals("#transparent")) triConsumer.accept(grid[z][x], x, z);
            }
        }
    }

}
