/**
 * 
 * Copyright (c) 2014, Nicholas Minkler
 * 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 *      are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *      this list of conditions and the following disclaimer in the documentation 
 *      and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 *      may be used to endorse or promote products derived from this software 
 *      without specific prior written permission.
 *
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
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
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.ArrayList;

public class NaiveMesher extends VoxelMesher {

    @Override
    public Mesh generateMesh(ChunkTerrain terrain) {
        ArrayList<Vector3f> verts = new ArrayList<>();
        ArrayList<Vector3f> textCoords = new ArrayList<>();
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
                        // Write the verts
                        switch (face) {
                            case TOP:
                                writeQuad(verts, indices, normals, 
                                        faceLoc_rearTopLeft, faceLoc_rearTopRight, faceLoc_frontTopLeft, faceLoc_frontTopRight, face);
                                break;
                            case BOTTOM:
                                 writeQuad(verts, indices, normals,
                                        faceLoc_rearBotRight, faceLoc_rearBotLeft, faceLoc_frontBotRight, faceLoc_frontBotLeft, face);
                                break;
                            case LEFT:
                                writeQuad(verts, indices, normals, 
                                        faceLoc_frontBotLeft, faceLoc_rearBotLeft, faceLoc_frontTopLeft, faceLoc_rearTopLeft, face);
                                break;
                            case RIGHT:
                                writeQuad(verts, indices, normals, 
                                        faceLoc_rearBotRight, faceLoc_frontBotRight, faceLoc_rearTopRight, faceLoc_frontTopRight, face);
                                break;
                            case FRONT:
                                writeQuad(verts, indices, normals, 
                                        faceLoc_rearBotLeft, faceLoc_rearBotRight, faceLoc_rearTopLeft, faceLoc_rearTopRight, face);
                                break;
                            case BACK:
                                writeQuad(verts, indices, normals, 
                                        faceLoc_frontBotRight, faceLoc_frontBotLeft, faceLoc_frontTopRight, faceLoc_frontTopLeft, face);
                                break;
                            default:       
                        }
                        // Write the texture coords, width and height is always 1 as we render each cube individually
                        this.writeTextureCoords(textCoords, terrain, tmpI, face, 1, 1, skin);
                    }
                }
            }
        }
        if (indices.isEmpty()) {
            return null;
        }
        // Return the generated mesh from the list of data
        return genMesh(verts, textCoords, indices, normals);
    }
}
