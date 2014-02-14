package com.jme3.cubed.render;

import com.jme3.cubed.Block;
import com.jme3.cubed.BlockSkin;
import com.jme3.cubed.BlockType;
import com.jme3.cubed.ChunkTerrain;
import com.jme3.cubed.Face;
import static com.jme3.cubed.Face.BACK;
import static com.jme3.cubed.Face.BOTTOM;
import static com.jme3.cubed.Face.FRONT;
import static com.jme3.cubed.Face.LEFT;
import static com.jme3.cubed.Face.RIGHT;
import static com.jme3.cubed.Face.TOP;
import com.jme3.cubed.MaterialManager;
import com.jme3.cubed.math.Vector3i;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;


/**
 *
 * @author Nicholas Minkler <sleaker@gmail.com>
 */
public class NaiveMesher extends VoxelMesher {

    @Override
    public Mesh generateMesh(ChunkTerrain terrain) {
        ArrayList<Vector3f> verts = new ArrayList<>();
        ArrayList<Vector2f> textCoords = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        Vector3f tmpLocation = new Vector3f();
        Vector3i tmpI = new Vector3i();
        for (int val = 0; val < terrain.getBlocks().length; val++) {
            ChunkTerrain.vectorFromInt555(val, tmpI);
            tmpLocation.setX(tmpI.getX()).setY(tmpI.getY()).setZ(tmpI.getZ());
            BlockType bt = MaterialManager.getInstance().getType(terrain.getBlocks()[val]);
            if (bt != null) {
                BlockSkin skin = bt.getSkin();
                Vector3f faceLoc_frontBotLeft = tmpLocation.add(Block.FRONT_BOTTOM_LEFT);
                Vector3f faceLoc_frontBotRight = tmpLocation.add(Block.FRONT_BOTTOM_RIGHT);
                Vector3f faceLoc_rearBotLeft = tmpLocation.add(Block.REAR_BOTTOM_LEFT);
                Vector3f faceLoc_rearBotRight = tmpLocation.add(Block.REAR_BOTTOM_RIGHT);
                Vector3f faceLoc_frontTopLeft = tmpLocation.add(Block.FRONT_TOP_LEFT);
                Vector3f faceLoc_frontTopRight = tmpLocation.add(Block.FRONT_TOP_RIGHT);
                Vector3f faceLoc_rearTopLeft = tmpLocation.add(Block.REAR_TOP_LEFT);
                Vector3f faceLoc_rearTopRight = tmpLocation.add(Block.REAR_TOP_RIGHT);
                // Loop over all 6 faces and verify which one should be visible as we currently don't cache this value
                for (Face face : Face.values()) {
                    if (terrain.isFaceVisible(tmpI, face)) {
                        switch (face) {
                            case TOP:
                                writeQuad(verts, textCoords, indices, normals, 
                                        faceLoc_rearTopLeft, faceLoc_rearTopRight, faceLoc_frontTopLeft, faceLoc_frontTopRight, 1, 1, face, skin);
                                break;
                            case BOTTOM:
                                 writeQuad(verts, textCoords, indices, normals,
                                        faceLoc_rearBotRight, faceLoc_rearBotLeft, faceLoc_frontBotRight, faceLoc_frontBotLeft, 1, 1, face, skin);
                                break;
                            case LEFT:
                                writeQuad(verts, textCoords, indices, normals, 
                                        faceLoc_frontBotLeft, faceLoc_rearBotLeft, faceLoc_frontTopLeft, faceLoc_rearTopLeft, 1, 1, face, skin);
                                break;
                            case RIGHT:
                                writeQuad(verts, textCoords, indices, normals, 
                                        faceLoc_rearBotRight, faceLoc_frontBotRight, faceLoc_rearTopRight, faceLoc_frontTopRight, 1, 1, face, skin);
                                break;
                            case FRONT:
                                writeQuad(verts, textCoords, indices, normals, 
                                        faceLoc_rearBotLeft, faceLoc_rearBotRight, faceLoc_rearTopLeft, faceLoc_rearTopRight, 1, 1, face, skin);
                                break;
                            case BACK:
                                writeQuad(verts, textCoords, indices, normals, 
                                        faceLoc_frontBotRight, faceLoc_frontBotLeft, faceLoc_frontTopRight, faceLoc_frontTopLeft, 1, 1, face, skin);
                                break;
                            default:
                                
                        }
                    }
                }
            }
        }
        if (indices.isEmpty()) {
            return null;
        }
        return genMesh(verts, textCoords, indices, normals);
    }
}
