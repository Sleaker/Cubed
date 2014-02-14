package com.jme3.cubed;

/**
 *
 * @author Carl
 */
public class BlockType {

    public BlockType(byte type, BlockSkin skin) {
        this.type = type;
        this.skin = skin;
    }

    private byte type;
    private BlockSkin skin;

    public byte getType() {
        return type;
    }

    public BlockSkin getSkin() {
        return skin;
    }
}
