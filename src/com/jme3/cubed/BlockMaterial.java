package com.jme3.cubed;

import com.jme3.asset.AssetManager;
import com.jme3.cubed.math.Vector2i;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.TextureArray;
import java.util.ArrayList;
import java.util.List;

public class BlockMaterial extends Material {

    private final Vector2i textSize;

    public BlockMaterial(AssetManager assetManager, String ... texturePaths){
        super(assetManager, "MatDefs/ShaderNodes/CubeShade.j3md");
        List<Image> images = new ArrayList<>();
        for (String path : texturePaths) {
            Texture texture = assetManager.loadTexture(path);
            images.add(texture.getImage());
        }
        TextureArray textArray = new TextureArray(images);
        textArray.setWrap(Texture.WrapMode.Repeat);
        textArray.setMagFilter(Texture.MagFilter.Nearest);
        textArray.setMinFilter(Texture.MinFilter.NearestNearestMipMap);
        setTexture("ColorMap", textArray);
        getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        textSize = new Vector2i(textArray.getImage().getWidth(), textArray.getImage().getHeight());
    }
    
    public int getTextureWidth() {
        return textSize.getX();
    }
    
    public int getTextureHeight() {
        return textSize.getY();
    }
    
    public Vector2i getTextWH() {
        return textSize;
    }
}
