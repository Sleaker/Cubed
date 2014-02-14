package com.jme3.cubed;

import com.jme3.math.Vector3f;

public class Block {

    /**
     * 0, 0, 0
     */
    public static final Vector3f FRONT_BOTTOM_LEFT = new Vector3f(0, 0, 0);
    /**
     * 1, 0, 0
     */
    public static final Vector3f FRONT_BOTTOM_RIGHT = new Vector3f(1, 0, 0);
    /**
     * 0, 0, 1
     */
    public static final Vector3f REAR_BOTTOM_LEFT = new Vector3f(0, 0, 1);
    /**
     * 1, 0, 1
     */
    public static final Vector3f REAR_BOTTOM_RIGHT = new Vector3f(1, 0, 1);
    /**
     * 0, 1, 0
     */
    public static final Vector3f FRONT_TOP_LEFT = new Vector3f(0, 1, 0);
    /**
     * 1, 1, 0
     */
    public static final Vector3f FRONT_TOP_RIGHT = new Vector3f(1, 1, 0);
    /**
     * 0, 1, 1
     */
    public static final Vector3f REAR_TOP_LEFT = new Vector3f(0, 1, 1);
    /**
     * 1, 1, 1
     */
    public static final Vector3f REAR_TOP_RIGHT = new Vector3f(1, 1, 1);
    
    private final BlockType type;

    public Block() {
        this.type = MaterialManager.getInstance().getType(getClass());
    }
    

    public BlockType getType() {
        return this.type;
    }
}
