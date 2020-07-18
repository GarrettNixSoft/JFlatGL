package com.floober.engine.renderEngine;

import com.floober.engine.display.Display;
import com.floober.engine.renderEngine.elements.geometry.CircleElement;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import org.joml.Vector4f;

/**
 * This Render class is left over from the way my old engine worked.
 * It has been repurposed to function in the new rendering system,
 * while allowing the rest of the game code to perform render calls
 * to add elements to the scene the same way as before.
 */
public class Render {

	// reference to the MasterRenderer to add elements
	public static MasterRenderer renderer;

	// RECTANGLES

	/**
	 * Draw a Rectangle.
	 * @param color The color of the rectangle (RGBA).
	 * @param x The x coordinate of the rectangle.
	 * @param y The y coordinate of the rectangle.
	 * @param z The z coordinate of the rectangle.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 * @param centered Whether to center the rectangle on (x,y). If false, (x,y) will be treated as the top-left corner.
	 */
	public static void drawRect(Vector4f color, float x, float y, float z, float width, float height, boolean centered) {
		renderer.addRectElement(new RectElement(color, x, y, z, width, height, centered));
	}

	/**
	 * Cover the screen with the specified color.
	 * @param color
	 */
	public static void fillScreen(Vector4f color) {
		renderer.addRectElement(new RectElement(color, 0, 0, 0, Display.WIDTH, Display.HEIGHT, false));
	}

	// CIRCLES

	/**
	 * Draw a circle.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param z The z coordinate of the circle.
	 * @param radius The radius of the circle.
	 */
	public static void drawCircle(Vector4f color, float x, float y, float z, float radius) {
		renderer.addCircleElement(new CircleElement(color, x, y, z, radius));
	}

	/**
	 * Draw a circle, with the inner radius cut out.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param z The z coordinate of the circle.
	 * @param innerRadius The inner radius of the circle.
	 * @param outerRadius The outer radius of the circle.
	 */
	public static void drawCircle(Vector4f color, float x, float y, float z, float innerRadius, float outerRadius) {
		renderer.addCircleElement(new CircleElement(color, x, y, z, innerRadius, outerRadius));
	}

	/**
	 * Draw a portion of a circle, starting with the specified offset, and continuing (360 * portion) degrees counterclockwise.
	 * @param color The color of the circle (RGBA).
	 * @param x The x coordinate of the circle.
	 * @param y The y coordinate of the circle.
	 * @param z The z coordinate of the circle.
	 * @param innerRadius The inner radius of the circle.
	 * @param outerRadius The outer radius of the circle.
	 * @param portion The percentage of the circle to draw.
	 * @param offset The offset from which to begin drawing. An offset of 0 indicates drawing starting from the top; a positive offset moves the starting position clockwise, in degrees.
	 */
	public static void drawCircle(Vector4f color, float x, float y, float z, float innerRadius, float outerRadius, float portion, float offset) {
		renderer.addCircleElement(new CircleElement(color, x, y, z, innerRadius, outerRadius, portion, offset));
	}

	/**
	 * Draw a horizontal or vertical line with the given start and end positions, and a width of 1 pixel.
	 * @param color The color of the line.
	 * @param x1 The starting x coordinate of the line.
	 * @param y1 The starting y coordinate of the line.
	 * @param x2 The ending x coordinate of the line.
	 * @param y2 The ending y coordinate of the line.
	 * @param z The z coordinate of the line.
	 */
	public static void drawLine(Vector4f color, float x1, float y1, float x2, float y2, float z) {
		renderer.addLineElement(new LineElement(color, x1, y1, x2, y2, z, 1));
	}

	/**
	 * Draw a horizontal or vertical line with the given start and end positions, and with the given width.
	 * @param color The color of the line.
	 * @param x1 The starting x coordinate of the line.
	 * @param y1 The starting y coordinate of the line.
	 * @param x2 The ending x coordinate of the line.
	 * @param y2 The ending y coordinate of the line.
	 * @param z The z coordinate of the line.
	 * @param lineWidth The width of the line.
	 */
	public static void drawLine(Vector4f color, float x1, float y1, float x2, float y2, float z, float lineWidth) {
		renderer.addLineElement(new LineElement(color, x1, y1, x2, y2, z, lineWidth));
	}

	/**
	 * Draw a rectangular outline at the given position, and with the given size.
	 * @param color The color of the outline.
	 * @param x The x coordinate of the outline.
	 * @param y The y coordinate of the outline.
	 * @param z The z coordinate of the outline.
	 * @param width The width of the outline.
	 * @param height The height of the outline.
	 * @param centered Whether the (x, y) position should be treated as the center of the outline. If false, it will be treated as the top-left corner.
	 */
	public static void drawOutline(Vector4f color, float x, float y, float z, float width, float height, boolean centered) {
		renderer.addOutlineElement(new OutlineElement(color, x, y, z, width, height, 1, centered));
	}

	/**
	 * Draw a rectangular outline at the given position, and with the given size and line width.
	 * @param color The color of the outline.
	 * @param x The x coordinate of the outline.
	 * @param y The y coordinate of the outline.
	 * @param z The z coordinate of the outline.
	 * @param width The width of the outline.
	 * @param height The height of the outline.
	 * @param lineWidth The width of the lines that make up the outline.
	 * @param centered Whether the (x, y) position should be treated as the center of the outline. If false, it will be treated as the top-left corner.
	 */
	public static void drawOutline(Vector4f color, float x, float y, float z, float width, float height, float lineWidth, boolean centered) {
		renderer.addOutlineElement(new OutlineElement(color, x, y, z, width, height, lineWidth, centered));
	}

	// draw tile

}