package com.floober.engine.animation;

import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.time.TimeScale;

public class Animation {

	// frames and counter
	protected TextureSet frames;
	protected int currentFrame;

	// tracking play
	protected boolean playedOnce;
	protected long startTime;
	protected int frameTime;

	// empty animations allowed but risky
	public Animation() {}

	public Animation(TextureSet frames, int frameTime) {
		this.frames = frames;
		this.frameTime = frameTime;
	}

	/**
	 * Create a static animation with only one frame.
	 * @param texComp the texture component for the single frame
	 */
	public Animation(TextureComponent texComp) {
		this.frames = new TextureSet(texComp, texComp.width(), texComp.height(), texComp.hasTransparency());
		this.frameTime = -1;
	}

	// GETTERS
	public TextureComponent getCurrentFrame() { return frames.getFrame(currentFrame); }

	public boolean hasPlayedOnce() {
		return playedOnce;
	}

	public TextureSet getFrames() {
		return frames;
	}
	public int getFrameTime() {
		return frameTime;
	}
	public int getCurrentFrameIndex() {
		return currentFrame;
	}

	// SETTERS
	public void setFrames(TextureSet frames) {
		this.frames = frames;
	}
	public void setFrameTime(int frameTime) {
		this.frameTime = frameTime;
	}

	// RUNNING THE ANIMATION
	public void update() {
		if (frameTime == -1) return;
		long elapsed = TimeScale.getScaledTime(startTime);
		if (elapsed > frameTime) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.getNumTextures()) {
			currentFrame = 0;
			playedOnce = true;
			startTime = System.nanoTime();
		}
	}

	public void reset() {
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
}