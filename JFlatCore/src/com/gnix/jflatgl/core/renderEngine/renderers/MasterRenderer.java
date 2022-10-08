package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.assets.loaders.GameLoader;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.display.Window;
import com.gnix.jflatgl.core.renderEngine.RenderLayer;
import com.gnix.jflatgl.core.renderEngine.batches.opaque.*;
import com.gnix.jflatgl.core.renderEngine.batches.transparent.*;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.*;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.fonts.fontRendering.FontRenderer;
import com.gnix.jflatgl.core.renderEngine.fonts.fontRendering.TextMaster;
import com.gnix.jflatgl.core.renderEngine.framebuffers.FrameBuffer;
import com.gnix.jflatgl.core.renderEngine.framebuffers.FrameBuffers;
import com.gnix.jflatgl.core.renderEngine.particles.ParticleMaster;
import com.gnix.jflatgl.core.renderEngine.particles.types.Particle;
import com.gnix.jflatgl.core.renderEngine.ppfx.PostProcessing;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import com.gnix.jflatgl.core.splash.SplashScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	private final ParticleMaster particleMaster;

	// Render layers
	private final RenderLayer[] layers;
	private final int RENDER_LAYERS;

	// Render batches; mapped by layer index
	private final Map<Integer, TextureBatchOpaque> opaqueTextureBatches = new HashMap<>();
	private final Map<Integer, TextureBatchTransparent> transparentTextureBatches = new HashMap<>();
	private final Map<Integer, GeometryBatchOpaque> opaqueGeometryBatches = new HashMap<>();
	private final Map<Integer, GeometryBatchTransparent> transparentGeometryBatches = new HashMap<>();

	// RENDER EXTENSIONS
	private final Map<Class<? extends RenderElement>, RenderExtension> renderExtensions = new HashMap<>();


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
		particleMaster = new ParticleMaster(RENDER_LAYERS);
		particleMaster.init();
		layers = new RenderLayer[Layers.NUM_LAYERS];
		initLayers();
		// add to instance list
		instances.put(window.getWindowID(), this);
		this.windowTarget = window.getWindowID();
	}

	public static void addRenderExtension(RenderExtension renderExtension) {
		currentRenderTarget.renderExtensions.put(renderExtension.getType(), renderExtension);
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
			opaqueGeometryBatches.put(i, new GeometryBatchOpaque(i, geometryRenderer));
			transparentGeometryBatches.put(i, new GeometryBatchTransparent(i, geometryRenderer));
		}
	}

	public static float getScreenZ(int layer) {
		int trueLayer = Layers.NUM_LAYERS - layer;
		return ((float) trueLayer / Layers.NUM_LAYERS);
	}

	public int getLayerByZ(float z) {
		return (int) (RENDER_LAYERS - (z * 10));
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

	public void addRectElement(RectElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentGeometryBatches.get(layer).addElement(element);
		else
			opaqueGeometryBatches.get(layer).addElement(element);
	}

	public void addRectLightElement(RectElementLight element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		transparentGeometryBatches.get(layer).addElement(element);
	}

	public void addCircleElement(CircleElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentGeometryBatches.get(layer).addElement(element);
		else
			opaqueGeometryBatches.get(layer).addElement(element);
	}

	public void addLineElement(LineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentGeometryBatches.get(layer).addElement(element);
		else
			opaqueGeometryBatches.get(layer).addElement(element);
	}

	public void addOutlineElement(OutlineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			transparentGeometryBatches.get(layer).addElement(element);
		else
			opaqueGeometryBatches.get(layer).addElement(element);
	}

	public void tryAddExtensionElement(RenderElement element) {
		if (renderExtensions.containsKey(element.getClass())) {
			RenderExtension extension = renderExtensions.get(element.getClass());
			extension.addElement(element);
		}
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
		if ((GameLoader.DATA_LOAD_COMPLETE || SplashScreen.SPLASH_RENDER) && useSceneBuffer) sceneBuffer.bindFrameBuffer();
		glDepthMask(true);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		if ((GameLoader.DATA_LOAD_COMPLETE || SplashScreen.SPLASH_RENDER) && useSceneBuffer) sceneBuffer.unbindFrameBuffer();

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
		if ((GameLoader.DATA_LOAD_COMPLETE || SplashScreen.SPLASH_RENDER) && useSceneBuffer) sceneBuffer.bindFrameBuffer();

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
		if ((GameLoader.DATA_LOAD_COMPLETE || SplashScreen.SPLASH_RENDER) && useSceneBuffer) sceneBuffer.unbindFrameBuffer();

		// Do post-processing to complete the frame render
		if ((GameLoader.DATA_LOAD_COMPLETE || SplashScreen.SPLASH_RENDER) && useSceneBuffer) postProcessor.doPostProcessing(sceneBuffer.getColorTexture());

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
			layer.addOpaqueBatch(opaqueTextureBatches.get(i));
			layer.addOpaqueBatch(opaqueGeometryBatches.get(i));
			layer.addTransparentBatch(transparentTextureBatches.get(i));
			layer.addTransparentBatch(transparentGeometryBatches.get(i));
			for (RenderExtension extension : renderExtensions.values()) {
				layer.addOpaqueBatch(extension.getOpaqueBatches().get(i));
				layer.addTransparentBatch(extension.getTransparentBatches().get(i));
			}
		}
	}

	private void clearBatches() {
		for (int i = 0; i < RENDER_LAYERS; ++i) {
			opaqueTextureBatches.get(i).clear();
			transparentTextureBatches.get(i).clear();
			opaqueGeometryBatches.get(i).clear();
			transparentGeometryBatches.get(i).clear();
			for (RenderExtension extension : renderExtensions.values()) {
				extension.getOpaqueBatches().get(i).clear();
				extension.getTransparentBatches().get(i).clear();
			}
		}
	}

	public static void cleanUp() {
		for (Window window : DisplayManager.getWindows()) {
			MasterRenderer windowRenderer = window.getWindowRenderer();
			windowRenderer.cleanUpInstance();
		}
	}

	public void cleanUpInstance() {
		sceneBuffer.cleanUp();
		textureRenderer.cleanUp();
		geometryRenderer.cleanUp();
		particleMaster.cleanUp();
	}

}
