package com.oberasoftware.home.rules.api.general;

import com.oberasoftware.home.rules.api.Block;

import java.util.List;

/**
 * @author Renze de Vries
 */
public class CompositeBlock implements Block {
    private List<Block> blocks;

    public CompositeBlock(List<Block> blocks) {
        this.blocks = blocks;
    }

    public CompositeBlock() {
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeBlock that = (CompositeBlock) o;

        return blocks.equals(that.blocks);

    }

    @Override
    public int hashCode() {
        return blocks.hashCode();
    }

    @Override
    public String toString() {
        return "CompositeBlock{" +
                "blocks=" + blocks +
                '}';
    }
}
