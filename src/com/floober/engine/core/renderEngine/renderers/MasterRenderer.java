package com.floober.engine.core.renderEngine.renderers;

import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.RenderLayer;
import com.floober.engine.core.renderEngine.batches.opaque.*;
import com.floober.engine.core.renderEngine.batches.transparent.*;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.*;
import com.floober.engine.core.renderEngine.elements.tile.TileElement;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffers;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.particles.ParticleTexture;
import com.floober.engine.core.renderEngine.particles.types.Particle;
import com.floober.engine.core.renderEngine.ppfx.PostProcessing;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.configuration.Config;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.*;

/**
 * A WIP replacement for the MasterRenderer class, using the new
 * RenderLayer/RenderBatch structure for rendering.
 */
public class MasterRenderer {

	public static final HashMap<Long, MasterRenderer> instances = new HashMap<>();
	public static MasterRenderer primaryWindowRenderer;
	public static MasterRenderer currentRenderTarget;

	// Window target
	private final long windowTarget;

	// game scene frame buffer
	private FrameBuffer sceneBuffer;

	// element renderers
	private final TextureRenderer textureRenderer;
	private final GeometryRenderer geometryRenderer;
	private final TileRenderer tileRenderer;

	private final ParticleMaster particleMaster;

	// Render layers
	private final RenderLayer[] layers;
	private final int RENDER_LAYERS;

	// Render batches; mapped by layer index
	private final Map<Integer, TextureBatchOpaque> opaqueTextureBatches = new HashMap<>();
	private final Map<Integer, TextureBatchTransparent> transparentTextureBatches = new HashMap<>();
	private final Map<Integer, TileBatchOpaque> opaqueTileBatches = new HashMap<>();
	private final Map<Integer, TileBatchTransparent> transparentTileBatches = new HashMap<>();
	private final Map<Integer, RectBatchOpaque> opaqueRectBatches = new HashMap<>();
	private final Map<Integer, RectBatchTransparent> transparentRectBatches = new HashMap<>();
	private final Map<Integer, CircleBatchOpaque> opaqueCircleBatches = new HashMap<>();
	private final Map<Integer, CircleBatchTransparent> transparentCircleBatches = new HashMap<>();
	private final Map<Integer, LineBatchOpaque> opaqueLineBatches = new HashMap<>();
	private final Map<Integer, LineBatchTransparent> transparentLineBatches = new HashMap<>();
	private final Map<Integer, OutlineBatchOpaque> opaqueOutlineBatches = new HashMap<>();
	private final Map<Integer, OutlineBatchTransparent> transparentOutlineBatches = new HashMap<>();

	private final Map<Integer, RectLightBatchTransparent> rectLightBatches = new HashMap<>();

	// Post-processing stage
	private PostProcessing postProcessor;

	/**
	 * Create a new MasterRenderer.
	 */
	public MasterRenderer(Window window) {
		// make this window's context current before creating any assets, buffers, etc.
		glfwMakeContextCurrent(window.getWindowID());
		if (primaryWindowRenderer == null) {
			primaryWindowRenderer = this;
			currentRenderTarget = this;
		}
		// determine the number of layers available
		RENDER_LAYERS = DisplayManager.getPrimaryGameWindow() == window ? Layers.NUM_LAYERS : Layers.EXTERN_LAYERS;
		// create the scene buffer
		sceneBuffer = FrameBuffers.createFullScreenFrameBuffer();
		// create the renderers
		textureRenderer = new TextureRenderer();
		geometryRenderer = new GeometryRenderer();
		tileRenderer = new TileRenderer();
		particleMaster = new ParticleMaster(RENDER_LAYERS);
		particleMaster.init();
		layers = new RenderLayer[Layers.NUM_LAYERS];
		initLayers();
		// create the post-processor
		postProcessor = new PostProcessing(window.getWindowID());
		// add to instance list
		instances.put(window.getWindowID(), this);
		this.windowTarget = window.getWindowID();
	}

	public MasterRenderer(Window window, boolean registered) {
		// make this window's context current before creating any assets, buffers, etc.
		glfwMakeContextCurrent(window.getWindowID());
		// determine the number of layers available
		RENDER_LAYERS = DisplayManager.getPrimaryGameWindow() == window ? Layers.NUM_LAYERS : Layers.EXTERN_LAYERS;
		// create the scene buffer
		if (registered)
			sceneBuffer = FrameBuffers.createFullScreenFrameBuffer();
		// create the renderers
		textureRenderer = new TextureRenderer();
		geometryRenderer = new GeometryRenderer();
		tileRenderer = new TileRenderer();
		particleMaster = new ParticleMaster(RENDER_LAYERS);
		particleMaster.init();
		layers = new RenderLayer[Layers.NUM_LAYERS];
		initLayers();
		// add to instance list
		instances.put(window.getWindowID(), this);
		this.windowTarget = window.getWindowID();
	}

	public void generateUnregisteredSceneBuffer(Window window) {
		sceneBuffer = FrameBuffers.createFullScreenFrameBufferUnregistered(window);
		// create the post-processor
		postProcessor = new PostProcessing(window.getWindowID());
	}

	public static long getTargetWindowID() {
		return currentRenderTarget.windowTarget;
	}

	public static Window getTargetWindow() {
		return DisplayManager.getWindow(currentRenderTarget.windowTarget);
	}

	public PostProcessing getPostProcessor() {
		return postProcessor;
	}

	public static int getParticleCount() {
		int total = 0;
		for (Long id : instances.keySet()) {
			total += instances.get(id).particleMaster.getParticleCount();
		}
		return total;
	}

//	public void setWindowTarget(long windowID) {
//		currentRenderTarget = instances.get(windowID);
//	}

	/**
	 * Get the frame buffer used to render the scene.
	 * @return the scene buffer
	 */
	public FrameBuffer getSceneBuffer() {
		return primaryWindowRenderer.sceneBuffer;
	}

	/**
	 * Create the render layers that will be used for rendering.
	 * Layers are dynamic, meaning increasing NUM_LAYERS will
	 * cause elements to automatically be sorted into the new number
	 * of layers based on their Z positions and the NEAR and FAR clip
	 * values defined in Config.
	 */
	private void initLayers() {
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			// create the layer
			layers[i] = new RenderLayer();
			// create the batches that will be used to populate the layer
			opaqueTextureBatches.put(i, new TextureBatchOpaque(i, textureRenderer));
			transparentTextureBatches.put(i, new TextureBatchTransparent(i, textureRenderer));
			opaqueTileBatches.put(i, new TileBatchOpaque(i, tileRenderer));
			transparentTileBatches.put(i, new TileBatchTransparent(i, tileRenderer));
			opaqueRectBatches.put(i, new RectBatchOpaque(i, geometryRenderer));
			transparentRectBatches.put(i, new RectBatchTransparent(i, geometryRenderer));
			opaqueCircleBatches.put(i, new CircleBatchOpaque(i, geometryRenderer));
			transparentCircleBatches.put(i, new CircleBatchTransparent(i, geometryRenderer));
			opaqueLineBatches.put(i, new LineBatchOpaque(i, geometryRenderer));
			transparentLineBatches.put(i, new LineBatchTransparent(i, geometryRenderer));
			opaqueOutlineBatches.put(i, new OutlineBatchOpaque(i, geometryRenderer));
			transparentOutlineBatches.put(i, new OutlineBatchTransparent(i, geometryRenderer));

			rectLightBatches.put(i, new RectLightBatchTransparent(i, geometryRenderer));
		}
	}

	public float getScreenZ(int layer) {
		int trueLayer = Layers.NUM_LAYERS - layer;
		return ((float) trueLayer / Layers.NUM_LAYERS);
	}

	public int getLayerByZ(float z) {
		return (int) (Layers.NUM_LAYERS - (z * 10));
	}

	// *** ADDING ELEMENTS ***

	public void addTextureElement(TextureElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.textureComponentHasTransparency())
			transparentTextureBatches.get(layer).addElement(element);
		else
			opaqueTextureBatches.get(layer).addElement(element);
	}

	public void addTileElement(TileElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentTileBatches.get(layer).addElement(element);
		else
			opaqueTileBatches.get(layer).addElement(element);
	}

	public void addTextElement(GUIText text) {
		// get the layer this element will be rendered in
		int layer = (int) text.getPosition().z();
//		TextMaster.
	}

	public void addRectElement(RectElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentRectBatches.get(layer).addElement(element);
		else
			opaqueRectBatches.get(layer).addElement(element);
	}

	public void addRectLightElement(RectElementLight element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		rectLightBatches.get(layer).addElement(element);
	}

	public void addCircleElement(CircleElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentCircleBatches.get(layer).addElement(element);
		else
			opaqueCircleBatches.get(layer).addElement(element);
	}

	public void addLineElement(LineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentLineBatches.get(layer).addElement(element);
		else
			opaqueLineBatches.get(layer).addElement(element);
	}

	public void addOutlineElement(OutlineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentOutlineBatches.get(layer).addElement(element);
		else
			opaqueOutlineBatches.get(layer).addElement(element);
	}

	public void addParticleInternal(Particle p) {
		particleMaster.addParticle(p);
	}

	public static void addParticle(Particle p) {
		currentRenderTarget.addParticleInternal(p);
	}

	// *** RENDERING ***
	public void prepare(boolean useSceneBuffer) {
		currentRenderTarget = this;
		if (GameLoader.LOAD_COMPLETE && useSceneBuffer) sceneBuffer.bindFrameBuffer();
		glDepthMask(true);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		if (GameLoader.LOAD_COMPLETE && useSceneBuffer) sceneBuffer.unbindFrameBuffer();

		// target the current window
		glfwMakeContextCurrent(windowTarget);
		if (useSceneBuffer) getTargetWindow().setViewport();
	}

	public void render(boolean useSceneBuffer) {

		// target the current window
//		glfwMakeContextCurrent(windowTarget);

		// update the particles
		particleMaster.update();

		// clear element counts
		GeometryRenderer.ELEMENT_COUNT = 0;
		TextureRenderer.ELEMENT_COUNT = 0;
		FontRenderer.ELEMENT_COUNT = 0;

		// render to scene buffer
		if (GameLoader.LOAD_COMPLETE && useSceneBuffer) sceneBuffer.bindFrameBuffer();

		// prepare
		prepareLayers();

		// RENDERING
		// render all opaque layers first, front to back
		for (int i = RENDER_LAYERS - 1; i >= 0; --i) {
			renderLayerOpaque(layers[i]);
		}
		// render transparent layers, back to front
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			renderLayerTransparent(layers[i]);
			particleMaster.renderParticles(i);
			TextMaster.render(i);
		}

		// clear
		clearBatches();

		// unbind scene buffer
		if (GameLoader.LOAD_COMPLETE && useSceneBuffer) sceneBuffer.unbindFrameBuffer();

		// Do post-processing to complete the frame render
		if (GameLoader.LOAD_COMPLETE && useSceneBuffer) postProcessor.doPostProcessing(sceneBuffer.getColorTexture());
		// TODO re-enable this when I figure out the rendering problems

	}

	private void renderLayerOpaque(RenderLayer layer) {
		layer.renderOpaque();
	}

	private void renderLayerTransparent(RenderLayer layer) {
		layer.renderTransparent();
	}

	private void prepareLayers() {
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			RenderLayer layer = layers[i];
			layer.addOpaqueBatch(opaqueTextureBatches.get((i)));
			layer.addOpaqueBatch(opaqueTileBatches.get((i)));
			layer.addOpaqueBatch(opaqueRectBatches.get((i)));
			layer.addOpaqueBatch(opaqueCircleBatches.get((i)));
			layer.addOpaqueBatch(opaqueLineBatches.get((i)));
			layer.addOpaqueBatch(opaqueOutlineBatches.get((i)));
			layer.addTransparentBatch(transparentTextureBatches.get(i));
			layer.addTransparentBatch(transparentTileBatches.get(i));
			layer.addTransparentBatch(transparentRectBatches.get(i));
			layer.addTransparentBatch(transparentCircleBatches.get(i));
			layer.addTransparentBatch(transparentLineBatches.get(i));
			layer.addTransparentBatch(transparentOutlineBatches.get(i));
			layer.addTransparentBatch(rectLightBatches.get(i));
		}
	}

	private void clearBatches() {
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			opaqueTextureBatches.get(i).clear();
			transparentTextureBatches.get(i).clear();
			opaqueTileBatches.get(i).clear();
			transparentTileBatches.get(i).clear();
			opaqueRectBatches.get(i).clear();
			transparentRectBatches.get(i).clear();
			opaqueCircleBatches.get(i).clear();
			transparentCircleBatches.get(i).clear();
			opaqueLineBatches.get(i).clear();
			transparentLineBatches.get(i).clear();
			opaqueOutlineBatches.get(i).clear();
			transparentOutlineBatches.get(i).clear();
			rectLightBatches.get(i).clear();
		}
	}

	public static void cleanUp() {
		for (Window window : DisplayManager.getWindows()) {
			MasterRenderer windowRenderer = window.getWindowRenderer();
			windowRenderer.sceneBuffer.cleanUp();
			windowRenderer.textureRenderer.cleanUp();
			windowRenderer.geometryRenderer.cleanUp();
			windowRenderer.tileRenderer.cleanUp();
			windowRenderer.particleMaster.cleanUp();
		}

	}

}