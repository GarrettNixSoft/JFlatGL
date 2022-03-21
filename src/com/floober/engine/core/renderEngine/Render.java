package com.floober.engine.core.renderEngine;

import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.*;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.configuration.Config;
import org.joml.Vector4f;

/**
 * This Render class is left over from the way my old engine worked.
 * It has been repurposed to function in the new rendering system,
 * while allowing the rest of the game code to perform render calls
 * to add elements to the scene the same way as before.
 */
public class Render {

	// MAIN RENDER METHODS

	public static void drawImage(TextureElement textureElement) {
		MasterRenderer.currentRenderTarget.addTextureElement(textureElement);
	}

	public static void drawText(GUIText text) { MasterRenderer.currentRenderTarget.addTextElement(text); }

	public static void drawRect(RectElement rectElement) {
		MasterRenderer.currentRenderTarget.addRectElement(rectElement);
	}

	public static void drawLightRect(RectElementLight element) { MasterRenderer.currentRenderTarget.addRectLightElement(element); }

	public static void drawCircle(CircleElement circleElement) {
		MasterRenderer.currentRenderTarget.addCircleElement(circleElement);
	}

	public static void drawLine(LineElement lineElement) {
		MasterRenderer.currentRenderTarget.addLineElement(lineElement);
	}

	public static void drawOutline(OutlineElement outlineElement) {
		MasterRenderer.currentRenderTarget.addOutlineElement(outlineElement);
	}


	/*
	 * The following section contains utility functions for generating
	 * elements to render on the fly and passing them to the MasterRenderer
	 * to be rendered. These should only be used for testing, or for
	 * convenience, but only sparingly; these objects will be held in collections
	 * by the MasterRenderer until the frame is complete, so they will (probably)
	 * not enjoy the benefits of short-lived object performance.
	 */

	// RECTANGLES

	/**
	 * Draw a Rectangle.
	 * @param color The color of the rectangle (RGBA).
	 * @param x The x coordinate of the rectangle.
	 * @param y The y coordinate of the rectangle.
	 * @param layer The z coordinate of the rectangle.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 * @param centered Whether to center the rectangle on (x,y). If false, (x,y) will be treated as the top-left corner.
	 */
	public static void drawRect(Vector4f color, float x, float y, int layer, float width, float height, boolean centered) {
		RectElement element = new RectElement(color, x, y, layer, width, height, centered);
		element.transform();
//		if (MasterRenderer.currentRenderTarget != MasterRenderer.primaryWindowRenderer)
//			Logger.log("Rect pos transformed = " + element.getPosition());
		MasterRenderer.currentRenderTarget.addRectElement(element);
	}

	/**
	 * Cover the screen with the specified color, placed at the back of the scene.
	 * @param color The color to fill.
	 */
	public static void fillScreen(Vector4f color) {
		MasterRenderer.currentRenderTarget.addRectElement(new RectElement(color, 0, 0, Layers.BOTTOM_LAYER, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, false));
	}

	/**
	 * Cover the screen with the specified color, placed at the specified z distance.
	 * @param color The color to fill.
	 */
	public static void fillScreen(Vector4f color, int layer) {
		MasterRenderer.currentRenderTarget.addRectElement(new RectElement(color, 0, 0, layer, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, false));
	}

	// CIRCLES

	/**
	 * Draw a circle.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param layer The z coordinate of the circle.
	 * @param radius The radius of the circle.
	 */
	public static void drawCircle(Vector4f color, float x, float y, int layer, float radius) {
		MasterRenderer.currentRenderTarget.addCircleElement(new CircleElement(color, x, y, layer, radius));
	}

	/**
	 * Draw a circle, with the inner radius cut out.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param layer The z coordinate of the circle.
	 * @param innerRadius The inner radius of the circle.
	 * @param outerRadius The outer radius of the circle.
	 */
	public static void drawCircle(Vector4f color, float x, float y, int layer, float innerRadius, float outerRadius) {
		MasterRenderer.currentRenderTarget.addCircleElement(new CircleElement(color, x, y, layer, innerRadius, outerRadius));
	}

	/**
	 * Draw a portion of a circle, starting with the specified offset, and continuing (360 * portion) degrees counterclockwise.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param layer The z coordinate of the circle.
	 * @param innerRadius The inner radius of the circle.
	 * @param outerRadius The outer radius of the circle.
	 * @param portion The percentage of the circle to draw.
	 * @param offset The offset from which to begin drawing. An offset of 0 indicates drawing starting from the top; a positive offset moves the starting position clockwise, in degrees.
	 */
	public static void drawCircle(Vector4f color, float x, float y, int layer, float innerRadius, float outerRadius, float portion, float offset) {
		MasterRenderer.currentRenderTarget.addCircleElement(new CircleElement(color, x, y, layer, innerRadius, outerRadius, portion, offset));
	}

	/**
	 * Draw a horizontal or vertical line with the given start and end positions, and a width of 1 pixel.
	 * @param color The color of the line.
	 * @param x1 The starting x coordinate of the line.
	 * @param y1 The starting y coordinate of the line.
	 * @param x2 The ending x coordinate of the line.
	 * @param y2 The ending y coordinate of the line.
	 * @param layer The z coordinate of the line.
	 */
	public static void drawLine(Vector4f color, float x1, float y1, float x2, float y2, int layer) {
		MasterRenderer.currentRenderTarget.addLineElement(new LineElement(color, x1, y1, x2, y2, layer, 1));
	}

	/**
	 * Draw a horizontal or vertical line with the given start and end positions, and with the given width.
	 * @param color The color of the line.
	 * @param x1 The starting x coordinate of the line.
	 * @param y1 The starting y coordinate of the line.
	 * @param x2 The ending x coordinate of the line.
	 * @param y2 The ending y coordinate of the line.
	 * @param layer The z coordinate of the line.
	 * @param lineWidth The width of the line.
	 */
	public static void drawLine(Vector4f color, float x1, float y1, float x2, float y2, int layer, float lineWidth) {
		MasterRenderer.currentRenderTarget.addLineElement(new LineElement(color, x1, y1, x2, y2, layer, lineWidth));
	}

	/**
	 * Draw a rectangular outline at the given position, and with the given size.
	 * @param color The color of the outline.
	 * @param x The x coordinate of the outline.
	 * @param y The y coordinate of the outline.
	 * @param layer The z coordinate of the outline.
	 * @param width The width of the outline.
	 * @param height The height of the outline.
	 * @param centered Whether the (x, y) position should be treated as the center of the outline. If false, it will be treated as the top-left corner.
	 */
	public static void drawOutline(Vector4f color, float x, float y, int layer, float width, float height, boolean centered) {
		MasterRenderer.currentRenderTarget.addOutlineElement(new OutlineElement(color, x, y, layer, width, height, 1, centered));
	}

	/**
	 * Draw a rectangular outline at the given position, and with the given size and line width.
	 * @param color The color of the outline.
	 * @param x The x coordinate of the outline.
	 * @param y The y coordinate of the outline.
	 * @param layer The z coordinate of the outline.
	 * @param width The width of the outline.
	 * @param height The height of the outline.
	 * @param lineWidth The width of the lines that make up the outline.
	 * @param centered Whether the (x, y) position should be treated as the center of the outline. If false, it will be treated as the top-left corner.
	 */
	public static void drawOutline(Vector4f color, float x, float y, int layer, float width, float height, float lineWidth, boolean centered) {
		MasterRenderer.currentRenderTarget.addOutlineElement(new OutlineElement(color, x, y, layer, width, height, lineWidth, centered));
	}

}