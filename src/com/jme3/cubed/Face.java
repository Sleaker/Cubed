package com.jme3.cubed;

import com.jme3.cubed.math.Vector3i;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nicholas
 */
public enum Face {
    TOP(new Vector3i(0, 1, 0)), 
    BOTTOM(new Vector3i(0, -1, 0)), 
    LEFT(new Vector3i(-1, 0, 0)), 
    RIGHT(new Vector3i(1, 0, 0)), 
    FRONT(new Vector3i(0, 0, 1)), 
    BACK(new Vector3i(0, 0, -1));
    
    private final Vector3i offset;
    private final List<Float> normals;
    
    Face(Vector3i offset) {
        this.offset = offset;
        normals = Collections.unmodifiableList(new ArrayList<>(Arrays.asList((float) offset.getX(), (float) offset.getY(), (float) offset.getZ())));
    }
    
    public Vector3i getOffsetVector() {
        return offset;
    }

    public Vector3i getNeighbor(Vector3i point) {
        return point.add(this.offset);
    }
    
    public Vector3i getNeightborLocal(Vector3i point) {
        return point.addLocal(this.offset);
    }

    public List<Float> getNormals() {
        return normals;
    }
}
