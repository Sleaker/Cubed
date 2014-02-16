package com.jme3.cubed;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.texture.Texture;

/**
 *
 * @author Carl
 */
public class BlockMaterial extends Material {

    public BlockMaterial(AssetManager assetManager, String texturePath){
        super(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        
        Texture texture = assetManager.loadTexture(texturePath);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
        setTexture("ColorMap", texture);
        getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
    }
}
