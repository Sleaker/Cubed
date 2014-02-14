package com.jme3.cubed.render;

import com.jme3.cubed.BlockSkin;
import com.jme3.cubed.ChunkTerrain;
import com.jme3.cubed.Face;
import com.jme3.cubed.math.Vector2i;
import com.jme3.cubed.math.Vector3i;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author Nicholas Minkler <sleaker@gmail.com>
 */


public abstract class VoxelMesher {
    public abstract Mesh generateMesh(ChunkTerrain terrain);
    
    protected Mesh genMesh(ArrayList<Vector3f> vertexList, ArrayList<Vector2f> textCoordsList, ArrayList<Integer> indicesList, ArrayList<Float> normalsList) {
        // Dump all of the Data into buffers on a Mesh
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertexList.toArray(new Vector3f[vertexList.size()])));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(textCoordsList.toArray(new Vector2f[textCoordsList.size()])));
        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = indicesList.get(i);
        }
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createIntBuffer(indices));
        if (normalsList != null) {
            float[] normals = new float[normalsList.size()];
            for (int i = 0; i < normals.length; i++) {
                normals[i] = normalsList.get(i);
            }
            mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        }
        mesh.updateBound();
        mesh.setStatic();
        return mesh;
    }

    /**
     * Dumps information for rendering a quad of the given width/height into the given lists
     * @param verts
     * @param textureCoords
     * @param indices
     * @param normals
     * @param bottomLeft
     * @param topLeft
     * @param topRight
     * @param bottomRight
     * @param width
     * @param height
     * @param face
     * @param skin 
     */
    protected void writeQuad(ArrayList<Vector3f> verts, ArrayList<Integer> indices, ArrayList<Float> normals,
            Vector3f bottomLeft, Vector3f topLeft, Vector3f topRight, Vector3f bottomRight, Face face) {
        //System.out.println("BL: " + bottomLeft + " BR: " + bottomRight + " TL: " + topLeft + " TR: " + topRight);
        // Get current vertex count, and add indices to the index list
        int vertCount = verts.size();
        indices.add(vertCount + 2);
        indices.add(vertCount + 0);
        indices.add(vertCount + 1);
        indices.add(vertCount + 1);
        indices.add(vertCount + 3);
        indices.add(vertCount + 2);

        // add the face normals
        normals.addAll(face.getNormals());
        
        // Add the verts in the necessary order
        verts.add(bottomLeft);
        verts.add(topLeft);
        verts.add(topRight);
        verts.add(bottomRight);
        
        
    }
    
    protected void writeTextureCoords(ArrayList<Vector2f> textureCoords, ChunkTerrain terrain, Vector3i blockLoc, Face face, int width, int height, BlockSkin skin) {
        // add texture locations from skin - need to adjust for width/height
        addBlockTextureCoordinates(textureCoords, skin.getTextureLocation(terrain, blockLoc, face), width, height);
    }
        
    private static void addBlockTextureCoordinates(ArrayList<Vector2f> textureCoordinatesList, Vector2i textureLoc, int width, int height){
        textureCoordinatesList.add(getTextureCoordinates(textureLoc, 0, 0));
        textureCoordinatesList.add(getTextureCoordinates(textureLoc, width, 0));
        textureCoordinatesList.add(getTextureCoordinates(textureLoc, 0, height));
        textureCoordinatesList.add(getTextureCoordinates(textureLoc, width, height));
    }

    private static Vector2f getTextureCoordinates(Vector2i textureLoc, int xUnitsToAdd, int yUnitsToAdd){
        float textureCount = 16;
        float textureUnit = 1.0f / textureCount;
        float x = (((textureLoc.getX() + xUnitsToAdd) * textureUnit));
        float y = ((((-1 * textureLoc.getY()) + (yUnitsToAdd - 1)) * textureUnit) + 1);
        return new Vector2f(x, y);
    }
}
