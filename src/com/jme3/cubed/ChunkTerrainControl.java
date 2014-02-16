package com.jme3.cubed;

import com.jme3.cubed.render.VoxelMesher;
import com.jme3.cubed.math.Vector3i;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nicholas Minkler <sleaker@gmail.com>
 */
public class ChunkTerrainControl extends AbstractControl {
    private Node chunkRoot;
    private BlockMaterial blockMaterial;
    private Map<Vector3i, ChunkTerrain> chunks;
    private boolean greedy = false;
    private VoxelMesher mesher;

    public ChunkTerrainControl(Node terrainNode, BlockMaterial bm, VoxelMesher mesher) {
        chunkRoot = terrainNode;
        this.blockMaterial = bm;
        this.mesher = mesher;
        chunks = new HashMap<>();
    }
    
    /**
     * Sends an update call to all <code>ChunkTerrain</code>'s attached to the the ChunkControl
     */
    @Override
    protected void controlUpdate(float tpf) {
        for (ChunkTerrain terrain : chunks.values()) {
            terrain.update(tpf);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    public void setBlock(Class<? extends Block> blockClass, Vector3i loc, boolean gen) {
        Vector3i pos = worldToChunkVec(loc);
        ChunkTerrain terrain = getTerrain(pos);
        if (terrain == null) {
            if (gen) {
                terrain = new ChunkTerrain(this, pos.getX(), pos.getY(), pos.getZ());
                chunkRoot.attachChild(terrain);
                chunks.put(pos, terrain);
            } else {
                return;
            }
        }
        terrain.setBlock(blockClass, loc.getX() & ChunkTerrain.MASK, loc.getY() & ChunkTerrain.MASK, loc.getZ() & ChunkTerrain.MASK);
    }
    
    public BlockMaterial getMaterial() {
        return blockMaterial;
    }
    
    /**
     * Gets a ChunkTerrain from the given chunk vector
     * @param vec
     * @return chunk
     */
    public ChunkTerrain getTerrain(Vector3i vec) {
        return chunks.get(vec);
    }
    
    /**
     * Gets a Chunk from the given Block vector
     * @param vec
     * @return chunk
     */
    public ChunkTerrain getTerrainLocal(Vector3i vec) {
        return getTerrain(worldToChunkVec(vec)); 
    }
    
    public byte getBlock(Vector3i vec) {
        ChunkTerrain terrain = getTerrainLocal(vec);
        if (terrain != null) {
            return terrain.getBlock(new Vector3i(vec.getX() & ChunkTerrain.MASK, vec.getY() & ChunkTerrain.MASK, vec.getZ() & ChunkTerrain.MASK));
        } else {
            return 0;
        }
    }
    private Vector3i worldToChunkVec(Vector3i vec) {
        return new Vector3i(vec.getX() >> ChunkTerrain.C_BITS, vec.getY() >> ChunkTerrain.C_BITS, vec.getZ() >> ChunkTerrain.C_BITS);
    }
    
    public void setMesher(VoxelMesher mesher) {
        this.mesher = mesher;
        switchMesher();
    }
    
    public VoxelMesher getMesher() {
        return this.mesher;
    }
        
    public void switchMesher() {
        this.greedy = !greedy;
        for (ChunkTerrain terrain : chunks.values()) {
            terrain.scheduleMeshUpdate();
        }
    }
}
