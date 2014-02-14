package com.jme3.cubed.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.cubed.BlockMaterial;
import com.jme3.cubed.BlockSkin;
import com.jme3.cubed.ChunkTerrain;
import com.jme3.cubed.ChunkTerrainControl;
import com.jme3.cubed.Face;
import com.jme3.cubed.MaterialManager;
import com.jme3.cubed.math.Vector2i;
import com.jme3.cubed.math.Vector3i;
import com.jme3.cubed.render.GreedyMesher;
import com.jme3.cubed.render.NaiveMesher;
import com.jme3.cubed.render.VoxelMesher;
import com.jme3.cubed.test.blocks.Block_Brick;
import com.jme3.cubed.test.blocks.Block_Grass;
import com.jme3.cubed.test.blocks.Block_Stone;
import com.jme3.cubed.test.blocks.Block_Water;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubedTest extends SimpleApplication{

    private MaterialManager mm;
    private BlockMaterial bm;
    private boolean wireframe = false;
    ChunkTerrainControl ctc;
    ArrayList<VoxelMesher> meshers = new ArrayList<>(Arrays.asList(new NaiveMesher(), new GreedyMesher()));
    private int currentMesh = 0;
    
    public static void main(String[] args){
        Logger.getLogger("").setLevel(Level.SEVERE);
        CubedTest app = new CubedTest();
        app.start();
    }

    public CubedTest(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cubes Test");
    }

    /**
     * ActionListener that swaps between wireframe and rendering modes
     */
    private ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("toggle wireframe") && !isPressed) {
                wireframe = !wireframe;
                bm.getAdditionalRenderState().setWireframe(wireframe);
            } else if (name.equals("toggle renderer") && !isPressed) {
                currentMesh++;
                if (currentMesh >= meshers.size()) {
                    currentMesh = 0;
                }
                ctc.setMesher(meshers.get(currentMesh));
            }
        }
    };

    @Override
    public void simpleInitApp(){
        this.stateManager.attach(new ScreenshotAppState());
        bm = new BlockMaterial(this.getAssetManager(), "Textures/cubes/terrain.png");
        bm.getAdditionalRenderState().setWireframe(wireframe);
        mm = MaterialManager.getInstance();
        initTestBlocks();
        //initializeWater();
        setupLighting();
        inputManager.addMapping("toggle wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("toggle renderer", new KeyTrigger(KeyInput.KEY_R));
        this.inputManager.addListener(actionListener, "toggle wireframe", "toggle renderer");

        //The terrain is a jME-Control, you can add it
        //to a node of the scenegraph to display it
        Node terrainNode = new Node("ChunkTerrain");
        ctc = new ChunkTerrainControl(terrainNode, bm, meshers.get(currentMesh));
        terrainNode.addControl(ctc);
        terrainNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.rootNode.attachChild(terrainNode);
        
        // TODO: finish fixing noise.
        //FractalNoise noise = new FractalNoise(null, 24.4f, 160, 160, 18);
        Vector3i point = new Vector3i();
        for (int x = 0; x < 160; x++) {
            point.setX(x);
            for (int y = 0; y < ChunkTerrain.C_SIZE; y++) {
                point.setY(y);
                for (int z = 0; z < 160; z++) {
                    point.setZ(z);
                    //ctc.setBlock(noise.getBlockAtPoint(point), point, true);
                }
            }
        }
        
        ctc.setBlock(Block_Stone.class, Vector3i.ZERO, true);
        ctc.setBlock(Block_Water.class, Vector3i.UNIT_Y, true);

        cam.setLocation(new Vector3f(-10, 10, 16));
        cam.lookAtDirection(new Vector3f(1, -0.56f, -1), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(50);
    }
    
    private void initTestBlocks() {
        mm.register(Block_Grass.class, new BlockSkin(new Vector2i[] { 
            new Vector2i(0, 0), new Vector2i(1, 0), new Vector2i(2, 0) }, false) {

            @Override
            protected int getTextureLocationIndex(Face face) {
                switch(face) {
                    case TOP:
                        return 0;

                    case BOTTOM:
                        return 2;
                }
                return 1;
            }
        });
        mm.register(Block_Stone.class, new BlockSkin(new Vector2i(9, 0), false));
        mm.register(Block_Water.class, new BlockSkin(new Vector2i(0, 1), true));
        mm.register(Block_Brick.class, new BlockSkin(new Vector2i(11, 0), false));
    }
    
    private void setupLighting() {
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setDirection(new Vector3f(-0.8f, -1, -0.8f).normalizeLocal());
        directionalLight.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
        getRootNode().addLight(directionalLight);
        getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), "Textures/cubes/sky.jpg", true));
        
        DirectionalLightShadowRenderer directionalLightShadowRenderer = new DirectionalLightShadowRenderer(getAssetManager(), 2048, 3);
        directionalLightShadowRenderer.setLight(directionalLight);
        directionalLightShadowRenderer.setShadowIntensity(0.3f);
        getViewPort().addProcessor(directionalLightShadowRenderer);
    }
    
    public void initializeWater() {
        WaterFilter waterFilter = new WaterFilter(getRootNode(), new Vector3f(-0.8f, -1, -0.8f).normalizeLocal());
        getFilterPostProcessor(this).addFilter(waterFilter);
    }
    
    private static FilterPostProcessor getFilterPostProcessor(SimpleApplication simpleApplication){
        List<SceneProcessor> sceneProcessors = simpleApplication.getViewPort().getProcessors();
        for(int i=0;i<sceneProcessors.size();i++){
            SceneProcessor sceneProcessor = sceneProcessors.get(i);
            if(sceneProcessor instanceof FilterPostProcessor){
                return (FilterPostProcessor) sceneProcessor;
            }
        }
        FilterPostProcessor filterPostProcessor = new FilterPostProcessor(simpleApplication.getAssetManager());
        simpleApplication.getViewPort().addProcessor(filterPostProcessor);
        return filterPostProcessor;
    }
}
