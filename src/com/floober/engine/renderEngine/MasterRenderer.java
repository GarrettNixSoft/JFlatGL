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

	private final Map<TextureAtlas, List<TileElement>> tileElements = new HashMap<>();

	public MasterRenderer() {
		textureRenderer = new TextureRenderer();
		tileRenderer = new TileRenderer();
		geometryRenderer = new GeometryRenderer();
	}

	// ADDING ELEMENTS (pre-render)
	public void addTextureElement(TextureElement textureElement) {
		textureElements.add(textureElement);
	}

	public void addTileElement(TileElement tileElement) {
		List<TileElement> elementList = tileElements.computeIfAbsent(tileElement.getTextureAtlas(), k -> new ArrayList<>());
		elementList.add(tileElement);
	}

	public void addRectElement(RectElement rectElement) {
		rectElements.add(rectElement);
	}

	public void addCircleElement(CircleElement circleElement) {
		circleElements.add(circleElement);
	}

	public void addLineElement(LineElement lineElement) {
		lineElements.add(lineElement);
	}

	public void addOutlineElement(OutlineElement outlineElement) {
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