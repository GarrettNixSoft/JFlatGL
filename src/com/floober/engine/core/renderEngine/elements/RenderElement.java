package com.floober.engine.core.renderEngine.elements;

import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.*;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffers;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.splash.SplashScreen;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class RenderElement implements Comparable<RenderElement> {

	protected float x, y;
	protected int layer;
	protected float width, height;
	protected float rotation;
	protected Vector3f position = new Vector3f();
	protected Vector2f scale = new Vector2f();
	protected boolean centered;
	protected boolean stencilWrite;

	public RenderElement(float x, float y, int layer, boolean centered) {
		assert layer <= Layers.TOP_LAYER && layer >= Layers.BOTTOM_LAYER;
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
		position = DisplayManager.convertToDisplayPosition(x, y, layer, width, height, centered);
//		if (centered) Logger.log("Position converted from (" + x + ", " + y + ") to " + position);
//		else Logger.log("Position converted from (" + (x+width/2) + ", " + (y+height/2) + ") to " + position);
		scale = new Vector2f(width, height);
	}

	public void transform(Window window) {
		position = DisplayManager.convertToDisplayPosition(window, x, y, layer, width, height, centered);
		scale = new Vector2f(width, height);
	}

	/**
	 * Render this RenderElement. Uses the new Java 17 preview feature, Pattern Matching for switch,
	 * to call the appropriate Render method for whatever type this RenderElement is.
	 */
	public void render() {
		switch (this) {
			case LineElement l -> Render.drawLine(l);
			case CircleElement c -> Render.drawCircle(c);
			case OutlineElement o -> Render.drawOutline(o);
			case RectElementLight rl -> Render.drawLightRect(rl);
			case RectElement r -> Render.drawRect(r);
			case TextureElement tex -> Render.drawImage(tex);
			default -> throw new IllegalStateException("Unexpected value: " + this);
		}
	}

	/**
	 * Call transform() and then render this element.
	 */
	public void renderTransformed() {
		if (SplashScreen.SPLASH_RENDER)
			SplashScreen.transform(this);
		else transform();

		switch (this) {
			case LineElement l -> Render.drawLine(l);
			case CircleElement c -> Render.drawCircle(c);
			case OutlineElement o -> Render.drawOutline(o);
			case RectElementLight rl -> Render.drawLightRect(rl);
			case RectElement r -> Render.drawRect(r);
			case TextureElement tex -> Render.drawImage(tex);
			default -> throw new IllegalStateException("Unexpected value: " + this);
		}
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
//		scale = new Vector2f(width, height); // TODO ?
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
	public boolean isStencilWrite() {
		return stencilWrite;
	}
	public Vector3f getPosition() {
		return position;
	}
	public Vector2f getScale() {
		return scale;
	}

	public Vector3f getPixelPosition() {
		return new Vector3f(x, y, layer);
	}

	public Vector3f getRenderPosition() {
		return new Vector3f(position).setComponent(2, MasterRenderer.primaryWindowRenderer.getScreenZ(layer));
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
	public void setStencilWrite(boolean stencilWrite) {
		this.stencilWrite = stencilWrite;
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
		return Integer.compare(other.layer, layer);
	}

}