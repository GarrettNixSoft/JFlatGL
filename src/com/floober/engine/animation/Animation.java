package com.floober.engine.animation;

import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.time.TimeScale;

public class Animation {

	// frames and counter
	protected Texture[] frames;
	protected int currentFrame;

	// tracking play
	protected boolean playedOnce;
	protected long startTime;
	protected int frameTime;

	// empty animations allowed but risky
	public Animation() {}

	public Animation(Texture[] frames, int frameTime) {
		this.frames = frames;
		this.frameTime = frameTime;
	}

	// GETTERS
	public Texture getCurrentFrame() { return frames[currentFrame]; }

	public boolean hasPlayedOnce() {
		return playedOnce;
	}

	public Texture[] getFrames() {
		return frames;
	}
	public int getFrameTime() {
		return frameTime;
	}
	public int getCurrentFrameIndex() {
		return currentFrame;
	}

	// SETTERS
	public void setFrames(Texture[] frames) {
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
		if (currentFrame == frames.length) {
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