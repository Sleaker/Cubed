package com.jme3.cubed;

import com.jme3.cubed.math.Vector3i;

public class BlockSkin {
    private int textureOffset = 0;
    private final String[] textureImages;
    private final boolean isTransparent;
    
    public BlockSkin(boolean isTransparent, String ... textureImages) {
        this.isTransparent = isTransparent;
        this.textureImages = textureImages;
    }
    
    /**
     * Gets the texture offset for this Skin. This is the location in the TextureArray where the Block texture is located
     * @param terrain
     * @param blockLoc
     * @param face
     * @return int texture offset
     */
    public int getTextureOffset(ChunkTerrain terrain, Vector3i blockLoc, Face face) {
        if(textureImages.length == 6) {
            return face.ordinal() + textureOffset;
        }
        return textureOffset;
    }

    public boolean isTransparent(){
        return isTransparent;
    }
    
    /**
     * Set this skin's offset value for textures in the TextureArray
     * @param offset 
     */
    public void setTextureOffset(int offset) {
        this.textureOffset = offset;
    }
    
    public String[] getTextureImages() {
        return textureImages;
    }
}
