package com.jme3.cubed;

import com.jme3.cubed.math.Vector3i;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import java.io.IOException;

/**
 *
 * @author Nicholas Minkler <sleaker@gmail.com>
 */
public class ChunkTerrain extends Node {

    public static final int C_SIZE = 32; // Length of a chunk
    public static final int C_BITS = 5; // Number of Bits allowed per chunk 32 >> 5 == 1;
    public static final int X_SHIFT = C_BITS; // Bits offset of the X value when packed into an int.
    public static final int Y_SHIFT = 10; // Bit offset of the Y value when packed into an int.
    public static final int Z_SHIFT = 0; // Bit offset of the Z value when packed into an int.
    public static final int MASK = C_SIZE - 1; // Mask is 1 less than the size.

    /**
     * Unpacks an int with 5 bits per value into a vector3.
     * @param val
     * @return Vector3
     */
    public static Vector3i vectorFromInt555(int val) {
        return new Vector3i((val >> X_SHIFT) & MASK, (val >> Y_SHIFT) & MASK, val & MASK);
    }

    /**
     * unpacks an int with 5 bits per value and stores it in the given Vector3i
     * @param val
     * @param to
     * @return the Vector3i 
     */
    public static Vector3i vectorFromInt555(int val, Vector3i to) {
        return to.setX((val >> X_SHIFT) & MASK).setY((val >> Y_SHIFT) & MASK).setZ(val & MASK);
    }

    private ChunkTerrainControl chunkControl;
    private Vector3i location;
    private Vector3i blockLocation;
    private Geometry meshData;
    private boolean needsMeshUpdate = false;
    private byte[] blocks;

    ChunkTerrain(ChunkTerrainControl ct, int x, int y, int z) {
        this.chunkControl = ct;
        this.location = new Vector3i(x, y, z);
        this.blockLocation = location.mult(C_SIZE);
        this.blocks = new byte[1 << (C_BITS * 3)];
        this.setLocalTranslation(blockLocation.toVector3f());
    }
    
    public ChunkTerrainControl getChunkControl() {
        return chunkControl;
    }

    protected void update(float tpf) {
        if (needsMeshUpdate) {
            if (meshData == null) {
                meshData = new Geometry("ChunkMesh: " + location.toString());
                this.attachChild(meshData);
                meshData.setMaterial(chunkControl.getMaterial());
            }
            long end, start; 
            start = System.nanoTime();
            Mesh mesh = chunkControl.getMesher().generateMesh(this);
            end = System.nanoTime();
            System.out.println("Meshed chunk: " + location.toString() + " in: " + (end - start) / 1000000f + " milliseconds");
            if (mesh != null) {
                meshData.setMesh(mesh);
            } else {
                this.detachChild(meshData);
            }
            needsMeshUpdate = false;
        }
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public void setBlock(Class<? extends Block> blockClass, int x, int y, int z) {
        if (isInChunk(x, y, z)) {
            if (blockClass == null) {
                blocks[(x << X_SHIFT) + z + (y << Y_SHIFT)] = 0;
            } else {
                blocks[(x << X_SHIFT) + z + (y << Y_SHIFT)] = MaterialManager.getInstance().getType(blockClass).getType();
            }
            needsMeshUpdate = true;
        } 
    }

    public void setBlock(Class<? extends Block> blockClass, Vector3i loc) {
        int x = loc.getX();
        int y = loc.getY();
        int z = loc.getZ();
        if (x < C_SIZE && y < C_SIZE && z < C_SIZE && x > -1 && y > -1 && z > -1) {
            if (blockClass == null) {
                blocks[(x << X_SHIFT) + z + ( y << Y_SHIFT)] = 0;
            } else {
                blocks[(x << X_SHIFT) + z + ( y << Y_SHIFT)] = MaterialManager.getInstance().getType(blockClass).getType();
            }
            needsMeshUpdate = true;
        }
    }
    
    public byte getBlock(int x, int y, int z) {
        if (isInChunk(x, y, z)) {
            return blocks[(x << X_SHIFT) + z + (y << Y_SHIFT)];
        } else {
            return 0;
        }
    }
    
    public byte getBlock(Vector3i loc) {
        return getBlock(loc.getX(), loc.getY(), loc.getZ());
    }
    
    public boolean isFaceVisible(Vector3i loc, Face face) {
        Vector3i vec = loc.add(face.getOffsetVector());
        byte type;
        if (!isInChunk(vec)) {
            type = chunkControl.getBlock(vec.add(blockLocation));
        } else {
            type = getBlock(loc.add(face.getOffsetVector()));
        }
        return type == 0 || MaterialManager.getInstance().getType(type).getSkin().isTransparent();
    }
    
    private boolean isInChunk(int x, int y, int z) {
        return x < C_SIZE && y < C_SIZE && z < C_SIZE && x > -1 && y > -1 && z > -1;
    }

    private boolean isInChunk(Vector3i vec) {
     return isInChunk(vec.getX(), vec.getY(), vec.getZ());  
    }

    public Vector3i getLocation() {
        return this.location;
    }
        
    public void scheduleMeshUpdate() {
        this.needsMeshUpdate = true;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);        
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(location, "location", Vector3i.ZERO);
        oc.write(blocks, "blocks", new byte[1 << (C_BITS * 3)]);
        
    }

    @Override
    public void read(JmeImporter in) throws IOException {
        super.read(in);
        InputCapsule ic = in.getCapsule(this);
        this.location = (Vector3i) ic.readSavable("location", Vector3i.ZERO);
        this.blockLocation = location.mult(C_SIZE);
        this.blocks = ic.readByteArray("blocks", new byte[1 << (C_BITS * 3)]);
        this.setLocalTranslation(blockLocation.toVector3f());
        this.needsMeshUpdate = true;
    }
}
