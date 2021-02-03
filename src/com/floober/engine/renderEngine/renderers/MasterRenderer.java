package com.floober.engine.renderEngine.renderers;

import com.floober.engine.loaders.GameLoader;
import com.floober.engine.renderEngine.RenderLayer;
import com.floober.engine.renderEngine.elements.geometry.*;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.fonts.fontRendering.FontRenderer;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.renderEngine.particles.ParticleMaster;
import com.floober.engine.renderEngine.batches.opaque.*;
import com.floober.engine.renderEngine.batches.transparent.*;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.util.Logger;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.input.KeyInput;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * A WIP replacement for the MasterRenderer class, using the new
 * RenderLayer/RenderBatch structure for rendering.
 */
public class MasterRenderer {

	public static final MasterRenderer instance = new MasterRenderer();

	// game scene frame buffer
	private final FrameBuffer sceneBuffer;

	// element renderers
	private final TextureRenderer textureRenderer;
	private final GeometryRenderer geometryRenderer;

	// Render layers
	private final RenderLayer[] layers;
	public static final int NUM_LAYERS = 10;
	public static final int TOP_LAYER = NUM_LAYERS - 1;

	// Render batches; mapped by layer index
	private final Map<Integer, TextureBatchOpaque> opaqueTextureBatches = new HashMap<>();
	private final Map<Integer, TextureBatchTransparent> transparentTextureBatches = new HashMap<>();
	private final Map<Integer, RectBatchOpaque> opaqueRectBatches = new HashMap<>();
	private final Map<Integer, RectBatchTransparent> transparentRectBatches = new HashMap<>();
	private final Map<Integer, CircleBatchOpaque> opaqueCircleBatches = new HashMap<>();
	private final Map<Integer, CircleBatchTransparent> transparentCircleBatches = new HashMap<>();
	private final Map<Integer, LineBatchOpaque> opaqueLineBatches = new HashMap<>();
	private final Map<Integer, LineBatchTransparent> transparentLineBatches = new HashMap<>();
	private final Map<Integer, OutlineBatchOpaque> opaqueOutlineBatches = new HashMap<>();
	private final Map<Integer, OutlineBatchTransparent> transparentOutlineBatches = new HashMap<>();

	private final Map<Integer, RectLightBatchTransparent> rectLightBatches = new HashMap<>();

	/**
	 * Create a new MasterRenderer.
	 */
	public MasterRenderer() {
		// create the scene buffer
		sceneBuffer = new FrameBuffer(Config.DEFAULT_RESOLUTION_WIDTH, Config.DEFAULT_RESOLUTION_HEIGHT, FrameBuffer.DEPTH_RENDER_BUFFER);
		// create the renderers
		textureRenderer = new TextureRenderer();
		geometryRenderer = new GeometryRenderer();
		layers = new RenderLayer[NUM_LAYERS];
		initLayers();
	}

	/**
	 * Get the frame buffer used to render the scene.
	 * @return the scene buffer
	 */
	public static FrameBuffer getSceneBuffer() {
		return instance.sceneBuffer;
	}

	/**
	 * Create the render layers that will be used for rendering.
	 * Layers are dynamic, meaning increasing NUM_LAYERS will
	 * cause elements to automatically be sorted into the new number
	 * of layers based on their Z positions and the NEAR and FAR clip
	 * values defined in Config.
	 */
	private void initLayers() {
		for (int i = 0; i < NUM_LAYERS; ++i) {
			// create the layer
			layers[i] = new RenderLayer();
			// create the batches that will be used to populate the layer
			opaqueTextureBatches.put(i, new TextureBatchOpaque(i, textureRenderer));
			transparentTextureBatches.put(i, new TextureBatchTransparent(i, textureRenderer));
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

	public static float getScreenZ(int layer) {
		int trueLayer = NUM_LAYERS - layer;
		return ((float) trueLayer / NUM_LAYERS);
	}

	public static int getLayerByZ(float z) {
		return (int) (NUM_LAYERS - (z * 10));
	}

	// *** ADDING ELEMENTS ***

	public static void addTextureElement(TextureElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			instance.transparentTextureBatches.get(layer).addElement(element);
		else
			instance.opaqueTextureBatches.get(layer).addElement(element);
	}

	public static void addTextElement(GUIText text) {
		// get the layer this element will be rendered in
		int layer = (int) text.getPosition().z();
//		TextMaster.
	}

	public static void addRectElement(RectElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			instance.transparentRectBatches.get(layer).addElement(element);
		else
			instance.opaqueRectBatches.get(layer).addElement(element);
	}

	public static void addRectLightElement(RectElementLight element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		instance.rectLightBatches.get(layer).addElement(element);
	}

	public static void addCircleElement(CircleElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			instance.transparentCircleBatches.get(layer).addElement(element);
		else
			instance.opaqueCircleBatches.get(layer).addElement(element);
	}

	public static void addLineElement(LineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			instance.transparentLineBatches.get(layer).addElement(element);
		else
			instance.opaqueLineBatches.get(layer).addElement(element);
	}

	public static void addOutlineElement(OutlineElement element) {
		// get the layer this element will be rendered in
		int layer = element.getLayer();
		// add this element to the appropriate batch
		if (element.hasTransparency())
			instance.transparentOutlineBatches.get(layer).addElement(element);
		else
			instance.opaqueOutlineBatches.get(layer).addElement(element);
	}

	// *** RENDERING ***
	public static void prepare() {
		if (GameLoader.LOAD_COMPLETE) instance.sceneBuffer.bindFrameBuffer();
		glDepthMask(true);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		if (GameLoader.LOAD_COMPLETE) instance.sceneBuffer.unbindFrameBuffer();
	}

	public static void render() {

		// clear element counts
		GeometryRenderer.ELEMENT_COUNT = 0;
		TextureRenderer.ELEMENT_COUNT = 0;
		FontRenderer.ELEMENT_COUNT = 0;

		// render to scene buffer
		if (GameLoader.LOAD_COMPLETE) instance.sceneBuffer.bindFrameBuffer();

		// prepare
		instance.prepareLayers();

		// RENDERING
		// render all opaque layers first, front to back
		for (int i = NUM_LAYERS - 1; i >= 0; --i) {
			instance.renderLayerOpaque(instance.layers[i]);
		}
		// render transparent layers, back to front
		for (int i = 0; i < NUM_LAYERS; ++i) {
			instance.renderLayerTransparent(instance.layers[i]);
			ParticleMaster.renderParticles(i);
			TextMaster.render(i);
		}

		// clear
		instance.clearBatches();

		// unbind scene buffer
		if (GameLoader.LOAD_COMPLETE) instance.sceneBuffer.unbindFrameBuffer();

	}

	private void renderLayerOpaque(RenderLayer layer) {
		layer.renderOpaque();
	}

	private void renderLayerTransparent(RenderLayer layer) {
		layer.renderTransparent();
	}

	private void prepareLayers() {
		for (int i = 0; i < NUM_LAYERS; ++i) {
			RenderLayer layer = layers[i];
			layer.addOpaqueBatch(opaqueTextureBatches.get((i)));
			layer.addOpaqueBatch(opaqueRectBatches.get((i)));
			layer.addOpaqueBatch(opaqueCircleBatches.get((i)));
			layer.addOpaqueBatch(opaqueLineBatches.get((i)));
			layer.addOpaqueBatch(opaqueOutlineBatches.get((i)));
			layer.addTransparentBatch(transparentTextureBatches.get(i));
			layer.addTransparentBatch(transparentRectBatches.get(i));
			layer.addTransparentBatch(transparentCircleBatches.get(i));
			layer.addTransparentBatch(transparentLineBatches.get(i));
			layer.addTransparentBatch(transparentOutlineBatches.get(i));
			layer.addTransparentBatch(rectLightBatches.get(i));
		}
	}

	private void clearBatches() {
		for (int i = 0; i < NUM_LAYERS; ++i) {
			opaqueTextureBatches.get(i).clear();
			transparentTextureBatches.get(i).clear();
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
		instance.sceneBuffer.cleanUp();
		instance.textureRenderer.cleanUp();
		instance.geometryRenderer.cleanUp();
	}

}