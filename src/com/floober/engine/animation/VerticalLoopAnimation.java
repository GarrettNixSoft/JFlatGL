package com.floober.engine.animation;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.textures.Texture;

/**
 * The VerticalLoopAnimation is an animation that repeats its
 * current frame vertically, scrolling up or down at a constant rate.
 */
public class VerticalLoopAnimation extends Animation {

	private final float moveSpeed;
	private final int loopHeight;
	private float yOffset;

	private final TextureElement textureElement;

	public VerticalLoopAnimation(Texture[] frames, int frameTime, float moveSpeed, int loopHeight) {
		this.moveSpeed = moveSpeed;
		this.loopHeight = loopHeight;
		setFrames(frames);
		setFrameTime(frameTime);
		textureElement = new TextureElement(getCurrentFrame(), 0, 0, 0, false);
	}

	public void update() {
		super.update();
		yOffset += moveSpeed * DisplayManager.getFrameTimeSeconds();
		yOffset %= loopHeight;
	}

	public void render(float x, float y, int layer, float w, float h, float alpha) {
		updateTextureElement(x, y + yOffset, layer, w, h, alpha);
		Render.drawImage(new TextureElement(textureElement));
		if (moveSpeed > 0) { // moving down, loop above
			updateTextureElement(x, y + yOffset - loopHeight, layer, w, h, alpha);
		}
		else { // moving up, loop below
			updateTextureElement(x, y + yOffset + loopHeight, layer, w, h, alpha);
		}
		Render.drawImage(new TextureElement(textureElement));
	}

	private void updateTextureElement(float x, float y, int layer, float width, float height, float alpha) {
		textureElement.setTexture(getCurrentFrame());
		textureElement.setPosition(x, y, layer);
		textureElement.setSize(width, height);
		textureElement.setAlpha(alpha);
		textureElement.transform();
	}

}
