package com.gnix.jflatgl.core.renderEngine.elements;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.textures.Texture;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TextureElement extends RenderElement {

	// standard texture data
	private TextureComponent textureComponent;

	// effect toggle
	private boolean doColorSwap;
	private boolean doColor;
	private boolean doLighting;
	private boolean doGlitch;
	private boolean doOutline;
	private boolean doFade;

	// color swap
	private final Vector4f rChannelColor = new Vector4f(0);
	private final Vector4f gChannelColor = new Vector4f(0);
	private final Vector4f bChannelColor = new Vector4f(0);
	private final Vector4f aChannelColor = new Vector4f(0);

	// alpha control
	private float alpha = 1;

	// color effect
	private final Vector4f color = new Vector4f(0);
	private float mix;

	// glitch effect
	private float glitchAmplitude;
	private float glitchSpeed;

	// outline effect
	private float outlineWidth;
	private final Vector4f outlineColor = new Vector4f(0);

	private Texture outlineTexture;
	private Vector2f outlineScale;

	// fade effect
	private float fadeMin = 0, fadeMax = 1;
	private float fadeStart = 0, fadeEnd = 1;
	private int fadeDirection = 0;

	public static final int FADE_LEFT_TO_RIGHT = 0;
	public static final int FADE_RIGHT_TO_LEFT = 1;
	public static final int FADE_TOP_TO_BOTTOM = 2;
	public static final int FADE_BOTTOM_TO_TOP = 3;

	/**
	 * Create an empty texture element.
	 * Do not use unless populating via setters.
	 */
	public TextureElement() {
		super(0, 0, 0, true);
		this.textureComponent = Game.getTexture("default");
		this.width = 0;
		this.height = 0;
//		setTextureOffset(new Vector4f(0, 0, 1, 1));
	}

	public TextureElement(TextureComponent textureComponent) {
		super(0, 0, 0, true);
		this.textureComponent = textureComponent;
		this.width = textureComponent.width();
		this.height = textureComponent.height();
	}

	public TextureElement(TextureComponent texture, float x, float y, int layer, float width, float height, boolean centered) {
		super(x, y, layer, centered);
		assert texture != null;
		this.textureComponent = texture;
		this.width = width;
		this.height = height;
		transform();
	}

	public TextureElement(TextureComponent texture, float x, float y, int layer, boolean centered) {
		super(x, y, layer, centered);
		assert texture != null;
		this.textureComponent = texture;
		this.width = texture.width();
		this.height = texture.height();
		transform();
	}

	public TextureElement(float x, float y, int layer, float width, float height, boolean centered) {
		super(x, y, layer, centered);
		this.textureComponent = Game.getTexture("default");
		this.width = width;
		this.height = height;
		transform();
		setTextureOffset(new Vector4f(0, 0, 1, 1));
	}

	// COPY CONSTRUCTOR
	public TextureElement(TextureElement other) {
		super(other.x, other.y, other.layer, other.centered);
		assert other.textureComponent != null;
		// texture and size
		this.textureComponent = new TextureComponent(other.textureComponent);
		this.width = other.width;
		this.height = other.height;
		// effect toggle
		this.doColorSwap = other.doColorSwap;
		this.doColor = other.doColor;
		this.doLighting = other.doLighting;
		this.doGlitch = other.doGlitch;
		this.doOutline = other.doOutline;
		this.doFade = other.doFade;
		// color swap
		this.rChannelColor.set(other.rChannelColor);
		this.gChannelColor.set(other.gChannelColor);
		this.bChannelColor.set(other.bChannelColor);
		this.aChannelColor.set(other.aChannelColor);
		// alpha control
		this.alpha = other.alpha;
		// color effect
		this.color.set(other.color);
		this.mix = other.mix;
		// glitch effect
		this.glitchAmplitude = other.glitchAmplitude;
		this.glitchSpeed = other.glitchSpeed;
		// outline effect
		this.outlineTexture = other.outlineTexture;
		this.outlineColor.set(other.outlineColor);
		this.outlineWidth = other.outlineWidth;
		this.outlineScale = other.outlineScale;
		// fade effect
		this.fadeMin = other.fadeMin;
		this.fadeMax = other.fadeMax;
		this.fadeStart = other.fadeStart;
		this.fadeEnd = other.fadeEnd;
		this.fadeDirection = other.fadeDirection;
		// finalize
		transform();
//		setTextureOffset(new Vector4f(0, 0, 1, 1));
	}

	// GETTERS
	public TextureComponent getTextureComponent() {
		return textureComponent;
	}
	// shortcuts
	public Texture getRawTexture() { return textureComponent.texture(); }
	public Vector4f getTextureComponentOffset() { return textureComponent.getTextureOffset(); }
	public float getAlpha() { return alpha; }
	public boolean textureComponentHasTransparency() { return textureComponent.hasTransparency(); }

	public boolean doColorSwap() {
		return doColorSwap;
	}
	public boolean doColor() { return doColor; }
	public boolean doLighting() { return doLighting; }
	public boolean doGlitch() { return doGlitch; }
	public boolean doOutline() {
		return doOutline;
	}
	public boolean doFade() { return doFade; }

	public Vector4f getrChannelColor() {
		return rChannelColor;
	}
	public Vector4f getgChannelColor() {
		return gChannelColor;
	}
	public Vector4f getbChannelColor() {
		return bChannelColor;
	}
	public Vector4f getaChannelColor() {
		return aChannelColor;
	}
	public Vector4f getColor() { return color; }
	public float getMix() { return mix; }

	public float getGlitchAmplitude() {
		return glitchAmplitude;
	}
	public float getGlitchSpeed() {
		return glitchSpeed;
	}

	public Vector2f stepSize() {
		return new Vector2f(outlineWidth / width, outlineWidth / height);
	}

	public Vector2f stepUnit() {
		return new Vector2f(1 / width, 1 / height);
	}

	public Vector4f outlineColor() { return outlineColor; }

	public Texture getOutlineTexture() { return outlineTexture; }
	public Vector2f getOutlineScale() { return outlineScale; }

	public Vector2f getFadeValues() { return new Vector2f(fadeMin, fadeMax); }
	public Vector2f getFadeOffsets() { return new Vector2f(fadeStart, fadeEnd); }
	public int getFadeDirection() { return fadeDirection; }

	// SETTERS
	public void setTexture(TextureComponent textureComponent) {
		this.textureComponent = textureComponent;
		this.width = textureComponent.width();
		this.height = textureComponent.height();
	}

	public void setTexture(TextureComponent texture, int width, int height) {
		this.textureComponent = texture;
		this.width = width;
		this.height = height;
	}

	public void setTextureOffset(Vector4f textureOffset) {
		this.textureComponent.setTextureOffset(textureOffset);
	}
	public void setHasTransparency(boolean hasTransparency) {
		this.textureComponent.setHasTransparency(hasTransparency);
	}

	public void setDoColorSwap(boolean doColorSwap) { this.doColorSwap = doColorSwap; }
	public void setDoColor(boolean doColor) { this.doColor = doColor; }
	public void setDoLighting(boolean doLighting) { this.doLighting = doLighting; }
	public void setDoGlitch(boolean doGlitch) { this.doGlitch = doGlitch; }
	public void setDoOutline(boolean doOutline) { this.doOutline = doOutline; }
	public void setDoFade(boolean doFade) {
		this.doFade = doFade;
	}

	public void setrChannelColor(Vector4f rChannelColor) { if (rChannelColor != null) this.rChannelColor.set(rChannelColor); }
	public void setgChannelColor(Vector4f gChannelColor) { if (gChannelColor != null) this.gChannelColor.set(gChannelColor); }
	public void setbChannelColor(Vector4f bChannelColor) { if (bChannelColor != null) this.bChannelColor.set(bChannelColor); }
	public void setaChannelColor(Vector4f aChannelColor) { if (aChannelColor != null) this.aChannelColor.set(aChannelColor); }

	public void setAlpha(float alpha) { this.alpha = alpha; }
	public void setColor(Vector4f color) { this.color.set(color); }
	public void setMix(float mix) { this.mix = mix; }

	public void setGlitchAmplitude(float glitchAmplitude) {
		this.glitchAmplitude = glitchAmplitude;
	}
	public void setGlitchSpeed(float glitchSpeed) {
		this.glitchSpeed = glitchSpeed;
	}

	public void setOutlineWidth(float outlineWidth) { this.outlineWidth = outlineWidth; }
	public void setOutlineColor(Vector4f outlineColor) { this.outlineColor.set(outlineColor); }

	public void setOutlineTexture(Texture outlineTexture) {
		this.outlineTexture = outlineTexture;
		this.outlineScale = DisplayManager.convertToDisplayScale(outlineTexture.width(), outlineTexture.height());
	}

	public void setFadeMin(float fadeMin) {
		this.fadeMin = fadeMin;
	}
	public void setFadeMax(float fadeMax) {
		this.fadeMax = fadeMax;
	}
	public void setFadeStart(float fadeStart) {
		this.fadeStart = fadeStart;
	}
	public void setFadeEnd(float fadeEnd) {
		this.fadeEnd = fadeEnd;
	}
	public void setFadeDirection(int fadeDirection) {
		this.fadeDirection = fadeDirection;
	}

}
