package com.gnix.jflatgl.element;

import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.renderEngine.Render;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import org.joml.Vector4f;

public class TileElement extends TextureElement {

	private final TextureAtlas textureAtlas;
	private final TextureAtlas alternateAtlas;
	private final Vector4f texOffsets;
	private int atlasIndex;
	private boolean hasTransparency;

	private boolean doOverlay;
	private TextureComponent overlayTexture;
	private float overlayAlpha;
	private float overlayRotation;

	public TileElement(TextureAtlas textureAtlas, TextureAtlas alternateAtlas, int atlasIndex, float x, float y, int layer, int tileSize) {
		super(x, y, layer, tileSize, tileSize, false);
		this.textureAtlas = textureAtlas;
		this.alternateAtlas = alternateAtlas;
		this.atlasIndex = atlasIndex;
		texOffsets = new Vector4f();
		setTextureOffset(atlasIndex);
	}

	public void useSettings(int index) {
		// look up settings for this value
		TileSettings settings = Tiles.mappedTileValues.containsKey(index) ? Tiles.mappedTileValues.get(index) : Tiles.mappedTileValues.get(-1);
		// get the custom index
		int atlasIndex = Tiles.mappedTileValues.containsKey(index) ? settings.atlasIndex() : index;
		// get the rotation
		setRotation(settings.rotation());
		// get the transparency value
		setHasTransparency(settings.hasTransparency());
		// get whether to color swap
		setDoColorSwap(settings.doColorSwap());
		// set color swap channels
		setrChannelColor(settings.rChannelColor());
		setgChannelColor(settings.gChannelColor());
		setbChannelColor(settings.bChannelColor());
		setaChannelColor(settings.aChannelColor());
		// get whether to use the overlay
		doOverlay = settings.doOverlay();
		// set overlay texture
		overlayTexture = settings.overlayTexture();
		// set overlay alpha
		overlayAlpha = settings.overlayAlpha();
		// set overlay rotation
		overlayRotation = settings.overlayRotation();
		// set the texture offsets for this tile
		setTextureOffset(atlasIndex);
	}

	// ******************************** RENDERING ********************************
	public void render(Camera camera) {
//		Render.drawElement(this);
		super.render(camera);
	}

	// ******************************** SELECTING TILE TEXTURE ********************************
	public void setTextureOffset(int index) {
		this.atlasIndex = index;
		int column = index % textureAtlas.numRows();
		int row = index / textureAtlas.numRows();
		int tileOffsetPixels = (int) (width * 0.05);
		float tileOffsetCoords = (float) tileOffsetPixels / textureAtlas.width(); // curse you, past me, for not explaining what this does		// you idiot, it divides pixels by width to get the [0..1] the shader expects
		texOffsets.x = (float) column / textureAtlas.numRows() + tileOffsetCoords;
		texOffsets.y = (float) row / textureAtlas.numRows() + tileOffsetCoords;
		texOffsets.z = texOffsets.x + 1f / textureAtlas.numRows() - tileOffsetCoords * 2;
		texOffsets.w = texOffsets.y + 1f / textureAtlas.numRows() - tileOffsetCoords * 2;
	}

	// ******************************** GETTERS ********************************
	public TextureAtlas getTextureAtlas() {
		if (atlasIndex < 0) return alternateAtlas;
		else return textureAtlas;
	}

	public Vector4f getTexOffsets() {
		return texOffsets;
	}
	public boolean hasTransparency() { return hasTransparency; }
	public boolean doOverlay() { return doOverlay; }
	public TextureComponent getOverlayTexture() { return overlayTexture; }
	public float getOverlayAlpha() { return overlayAlpha; }
	public float getOverlayRotation() { return overlayRotation; }

	// ******************************** SETTERS ********************************
	public void setTileSize(float tileSize) {
		setSize(tileSize, tileSize);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		transform();
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

}
