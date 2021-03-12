package com.floober.engine.background;

import com.floober.engine.animation.Animation;
import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.renderEngine.textures.TextureSet;

/**
 * @author Floober
 * 
 * The base of the background, the image looped in the back.
 * 
 */
public class BackgroundBase {

	// animated
	private final Animation animation;
	private final TextureElement textureElement;
	
	// location/speed
	private float x;
	private float y;
	private final float dx;

	private int xCount, yCount; // times to tile background to fill screen
	
	public BackgroundBase(TextureSet textures, int animationTime, float moveSpeed) {
		animation = new Animation();
		animation.setFrames(textures);
		animation.setFrameTime(animationTime);
		textureElement = new TextureElement(animation.getCurrentFrame(), x, y, 0, false);
		this.dx = -moveSpeed;
		setSize();
	}

	private void setSize() {
		TextureComponent texture = animation.getFrames().getFrame(0);
		int width = texture.width();
		int height = texture.height();
		xCount = Math.max(((Display.WIDTH + 1) / width) + 1, 2) + 1;
		yCount = Math.max(((Display.HEIGHT + 1) / height) + 1, 2) + 1;
//		Logger.log("Background will be tiled: " + xCount + "x" + yCount);
	}
	
	public void update() {
		animation.update();
		float frameTime = DisplayManager.getFrameTimeSeconds();
		x += (dx * frameTime);
		x %= animation.getCurrentFrame().width();
	}
	
	public void render() {
		// loop
		for (int i = 0; i < yCount; ++i) {
			for (int j = 0; j < xCount; ++j) {
				updateTextureElement(x + j * textureElement.getWidth(), y + i * textureElement.getHeight(), textureElement.getWidth(), textureElement.getHeight());
				Render.drawImage(new TextureElement(textureElement));
			}
		}
	}

	private void updateTextureElement(float x, float y, float width, float height) {
		textureElement.setTexture(animation.getCurrentFrame());
		textureElement.setPosition(x, y, 0);
		textureElement.setSize(width, height);
		textureElement.transform();
	}
	
}