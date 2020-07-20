package com.floober.engine.renderEngine;

import com.floober.engine.models.ModelLoader;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.elements.TileElement;
import com.floober.engine.renderEngine.elements.geometry.CircleElement;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class MasterRenderer {

	// element renderers
	private final TextureRenderer textureRenderer;
	private final TileRenderer tileRenderer;
	private final GeometryRenderer geometryRenderer;

	// element lists
	private final List<TextureElement> textureElements = new ArrayList<>();
	private final List<RectElement> rectElements = new ArrayList<>();
	private final List<CircleElement> circleElements = new ArrayList<>();
	private final List<LineElement> lineElements = new ArrayList<>();
	private final List<OutlineElement> outlineElements = new ArrayList<>();

	// tiles, which are instance rendered
	private final Map<TextureAtlas, List<TileElement>> tileElements = new HashMap<>();

	// Element limits! These are hard limits; any attempts to add new elements when these limits are met will be ignored until the next frame begins, and the counts are reset.
	public static final int MAX_TEXTURE_INSTANCES = 200;
	public static final int MAX_RECT_INSTANCES = 100;
	public static final int MAX_CIRCLE_ELEMENTS = 100;
	public static final int MAX_LINE_INSTANCES = 100;
	public static final int MAX_OUTLINE_INSTANCES = 50;

	public MasterRenderer() {
		textureRenderer = new TextureRenderer();
		tileRenderer = new TileRenderer();
		geometryRenderer = new GeometryRenderer();
	}

	// CHECKING FOR AVAILABILITY
	public boolean textureElementsFull() {
		return textureElements.size() >= MAX_TEXTURE_INSTANCES;
	}

	public boolean rectElementsFull() {
		return rectElements.size() >= MAX_RECT_INSTANCES;
	}

	public boolean circleElementsFull() {
		return circleElements.size() >= MAX_CIRCLE_ELEMENTS;
	}

	public boolean lineElementsFull() {
		return lineElements.size() >= MAX_LINE_INSTANCES;
	}

	public boolean outlineElementsFull() {
		return outlineElements.size() >= MAX_OUTLINE_INSTANCES;
	}

	// ADDING ELEMENTS (pre-render)
	/**
	 * Add a Texture element to the scene to be rendered.
	 * Fails if the Texture instance limit has already been met for this frame.
	 * @param textureElement The TextureElement to render.
	 */
	public void addTextureElement(TextureElement textureElement) {
		if (textureElements.size() < MAX_TEXTURE_INSTANCES)
			textureElements.add(textureElement);
	}

	public void addTileElement(TileElement tileElement) {
		List<TileElement> elementList = tileElements.computeIfAbsent(tileElement.getTextureAtlas(), k -> new ArrayList<>());
		elementList.add(tileElement);
	}

	/**
	 * Add a Rectangle element to the scene to be rendered.
	 * Fails if the Rectangle instance limit has already been met for this frame.
	 * @param rectElement The RectElement to render.
	 */
	public void addRectElement(RectElement rectElement) {
		if (rectElements.size() < MAX_RECT_INSTANCES)
			rectElements.add(rectElement);
	}

	/**
	 * Add a Circle element to the scene to be rendered.
	 * Fails if the Circle instance limit has already been met for this frame.
	 * @param circleElement The CircleElement to render.
	 */
	public void addCircleElement(CircleElement circleElement) {
		if (circleElements.size() < MAX_CIRCLE_ELEMENTS)
			circleElements.add(circleElement);
	}

	/**
	 * Add a Line element to the scene to be rendered.
	 * Fails if the Line instance limit has already been met for this frame.
	 * @param lineElement The LineElement to render.
	 */
	public void addLineElement(LineElement lineElement) {
		if (lineElements.size() < MAX_LINE_INSTANCES)
			lineElements.add(lineElement);
	}

	/**
	 * Add an Outline element to the scene to be rendered.
	 * Fails if the Outline instance limit has already been met for this frame.
	 * @param outlineElement The OutlineElement to render.
	 */
	public void addOutlineElement(OutlineElement outlineElement) {
		if (outlineElements.size() < MAX_OUTLINE_INSTANCES)
			outlineElements.add(outlineElement);
	}

	// RENDERING
	public void prepare() {
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Render all elements currently in the MasterRenderer to the
	 * scene, then clear the element lists.
	 */
	public void render() {

		// render tile elements
		tileRenderer.render(tileElements);

		// render texture elements; these are sorted by Z value, no need to sort them
		textureRenderer.render(textureElements);

		// sort geometry elements
		Collections.sort(rectElements);
		Collections.sort(circleElements);
		Collections.sort(lineElements);
		Collections.sort(outlineElements);

		// render geometry elements
		geometryRenderer.renderRectangles(rectElements);
		geometryRenderer.renderCircles(circleElements);
		geometryRenderer.renderLines(lineElements);
		geometryRenderer.renderOutlines(outlineElements);

		// clear all elements
		tileElements.clear();
		textureElements.clear();
		rectElements.clear();
		circleElements.clear();
		lineElements.clear();
		outlineElements.clear();

	}

	public void cleanUp() {
		textureRenderer.cleanUp();
		geometryRenderer.cleanUp();
	}

}