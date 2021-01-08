package com.floober.engine.renderEngine.elements;

import com.floober.engine.display.Display;
import com.floober.engine.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.renderEngine.framebuffers.FrameBuffers;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class RenderElement implements Comparable<RenderElement> {

	protected float x, y;
	protected int layer;
	protected float width, height;
	protected float rotation;
	protected Vector3f position;
	protected Vector2f scale;
	protected boolean centered;

	public RenderElement(float x, float y, int layer, boolean centered) {
		this.x = x;
		this.y = y;
		this.layer = layer;
		this.centered = centered;
	}

	/**
	 * Apply any changes to this element's position or size
	 * to its appearance on screen, by converting their current
	 * values from pixel units to screen units.
	 */
	public void transform() {
		position = Display.convertToDisplayPosition(x, y, layer, width, height, centered);
		scale = Display.convertToDisplayScale(width, height);
	}

	/**
	 * Apply any changes to this element's position or size
	 * to its appearance within the given FrameBuffer by converting
	 * their current values from pixel units to screen units.
	 * @param buffer The FrameBuffer to use for reference.
	 */
	public void transform(FrameBuffer buffer) {
		position = FrameBuffers.convertToFramebufferPosition(x, y, layer, width, height, centered, buffer);
		scale = FrameBuffers.convertToFramebufferScale(width, height, buffer);
	}

	// GETTERS
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getLayer() {
		return layer;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public float getRotation() { return rotation; }
	public boolean isCentered() {
		return centered;
	}
	public Vector3f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}

	public Vector3f getRenderPosition() {
		return new Vector3f(position).setComponent(2, MasterRenderer.getScreenZ(layer));
	}

	// SETTERS
	public void setX(float x) {
		this.x = x;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public void setRotation(float rotation) { this.rotation = rotation; }
	public void setCentered(boolean centered) {
		this.centered = centered;
	}

	public void setPosition(float x, float y, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
	}

	public void setPosition(Vector3f position) {
		this.x = position.x;
		this.y = position.y;
		this.layer = (int) position.z;
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setSize(Vector2f size) {
		this.width = size.x;
		this.height = size.y;
	}

	@Override
	public int compareTo(RenderElement other) {
		return Float.compare(other.layer, layer);
	}

}