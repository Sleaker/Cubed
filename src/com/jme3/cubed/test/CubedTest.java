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
import com.jme3.cubed.math.Vector3i;
import com.jme3.cubed.render.GreedyMesher;
import com.jme3.cubed.render.NaiveMesher;
import com.jme3.cubed.render.VoxelMesher;
import com.jme3.cubed.test.blocks.Block_Dirt;
import com.jme3.cubed.test.blocks.Block_Grass;
import com.jme3.cubed.test.blocks.Block_Stone;
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
import net.jlibnoise.NoiseQuality;
import net.jlibnoise.module.source.Perlin;

public class CubedTest extends SimpleApplication {

    // Random seed value, adjust this to try different test terrain
    private static final int DEFAULT_SEED = 124124;
    private MaterialManager mm;
    private BlockMaterial bm;
    private boolean wireframe = false;
    ChunkTerrainControl ctc;
    ArrayList<VoxelMesher> meshers = new ArrayList<>(Arrays.asList(new NaiveMesher(), new GreedyMesher()));
    private int currentMesh = 0;

    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.SEVERE);
        CubedTest app = new CubedTest();
        app.start();
    }

    public CubedTest() {
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
    public void simpleInitApp() {
        this.stateManager.attach(new ScreenshotAppState());
        mm = MaterialManager.getInstance();
        initTestBlocks();
        bm = new BlockMaterial(this.getAssetManager(), mm.getTexturePaths());
        bm.getAdditionalRenderState().setWireframe(wireframe);
        
        
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
        this.genTerrainFromNoise(ctc);

        //ctc.setBlock(Block_Stone.class, Vector3i.ZERO, true);
        //ctc.setBlock(Block_Water.class, Vector3i.UNIT_Y, true);

        cam.setLocation(new Vector3f(-10, 64, 16));
        cam.lookAtDirection(new Vector3f(1, -0.56f, -1), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(50);
    }

    private void initTestBlocks() {
        mm.register(Block_Grass.class, new BlockSkin(false, "Textures/cubes/grass_top.png", "Textures/cubes/grass_side.png", "Textures/cubes/dirt.png") {
            @Override
            public int getTextureOffset(ChunkTerrain terrain, Vector3i blockLoc, Face face) {
                if (terrain.isFaceVisible(blockLoc, Face.TOP)) {
                    switch (face) {
                        case TOP:
                            return 0;

                        case BOTTOM:
                            return 2;
                    }
                    return 1;
                }
                return 2;
            }
        });
        mm.register(Block_Stone.class, new BlockSkin(false, "Textures/cubes/stone.png"));
        mm.register(Block_Dirt.class, new BlockSkin(false, "Textures/cubes/dirt.png"));
        //mm.register(Block_Water.class, new BlockSkin(true, ""));
        //mm.register(Block_Brick.class, new BlockSkin(new Vector2i(11, 0), false));
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

    private static FilterPostProcessor getFilterPostProcessor(SimpleApplication simpleApplication) {
        List<SceneProcessor> sceneProcessors = simpleApplication.getViewPort().getProcessors();
        for (int i = 0; i < sceneProcessors.size(); i++) {
            SceneProcessor sceneProcessor = sceneProcessors.get(i);
            if (sceneProcessor instanceof FilterPostProcessor) {
                return (FilterPostProcessor) sceneProcessor;
            }
        }
        FilterPostProcessor filterPostProcessor = new FilterPostProcessor(simpleApplication.getAssetManager());
        simpleApplication.getViewPort().addProcessor(filterPostProcessor);
        return filterPostProcessor;
    }

    private void genTerrainFromNoise(ChunkTerrainControl ctc) {
        Perlin noise = new Perlin();
        noise.setFrequency(0.01);
        noise.setLacunarity(2.0);
        noise.setNoiseQuality(NoiseQuality.BEST);
        noise.setOctaveCount(8);
        noise.setPersistence(0.5);
        noise.setSeed(DEFAULT_SEED);
        Vector3i point = new Vector3i();
        for (int x = -160; x < 160; x++) {
            point.setX(x);
            for (int z = -160; z < 160; z++) {
                int landHeight = (int) (Math.floor(noise.getValue(x, 0, z) * 20 + 20));
                int dirtHeight = (int) (Math.floor((hash(x, z) >> 8 & 0xf) / 15f * 3) + 1);
                point.setZ(z);
                for (int y = 0; y < 64; y++) {
                    point.setY(y);
                    if (y == 0) {
                        ctc.setBlock(Block_Stone.class, point, true);
                    } else if (y > landHeight)
                            continue;
                    if (y == landHeight) {
                        ctc.setBlock(Block_Grass.class, point, true);
                    } else if (y >= landHeight - dirtHeight) {
                        ctc.setBlock(Block_Dirt.class, point, true);
                    } else {
                        ctc.setBlock(Block_Stone.class, point, true);
                    }
                }
            }
        }
    }

    private static int hash(int x, int y) {
        int hash = x * 3422543 ^ y * 432959;
        return hash * hash * (hash + 324319);
    }
}
