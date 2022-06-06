package com.floober.engine.animation;

import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.math.RandomUtil;
import com.floober.engine.core.util.time.TimeScale;

/**
 * A Glitch Animation is an extension of the Animation class that
 * adds a random glitch effect using a separate array of pre-made
 * "glitched" textures that get swapped in for the "normal" animation
 * frames for random durations at random intervals, the timing of each
 * event being determined by the object using this Glitch Animation.
 */
public class GlitchAnimation extends Animation {

	// alternate frame set
	private TextureSet glitchFrames;
	private int currentGlitchFrame;
	private TextureComponent glitchFrame;

	// unused? but nice to have? not really
	private boolean glitchedOnce;

	// timing
	private long glitchStartTime;
	private long glitchFrameTimer;
	private int glitchDelay;
	private int glitchDuration;

	// frequency of glitching
//	private int glitchWait;
//	private int minWait, maxWait;

	// duration and severity of glitching
	private boolean glitching;

	public GlitchAnimation(GlitchAnimation other) {
		super(other.frames, other.frameTime);
		// super fields
		this.currentFrame = other.currentFrame;
		// fields
		this.glitchFrames = other.glitchFrames;
		this.currentGlitchFrame = other.currentGlitchFrame;
		this.glitchFrame = other.glitchFrame;
		this.glitchedOnce = other.glitchedOnce;
		this.glitchStartTime = other.glitchStartTime;
		this.glitchFrameTimer = other.glitchFrameTimer;
		this.glitchDelay = other.glitchDelay;
		this.glitchDuration = other.glitchDuration;
		// ...
		this.glitching = other.glitching;
	}

	/**
	 * Create a glitch animation from a normal animation by adding a glitch frame array.
	 * @param animation The original animation to base this new glitch animation off of.
	 * @param glitchFrames The glitch frames to add to this animation.
	 */
	public GlitchAnimation(Animation animation, TextureSet glitchFrames) {
		super(animation.getFrames(), animation.getFrameTime());
		currentFrame = animation.getCurrentFrameIndex();
		setGlitchFrames(glitchFrames);
	}

	@Override
	public TextureComponent getCurrentFrame() {
		if (glitching) return glitchFrame;
		else return super.getCurrentFrame();
	}
	
	public void setGlitchFrames(TextureSet frames) {
		this.glitchFrames = frames;
		currentGlitchFrame = 0;
		glitchedOnce = false;
	}
	
	public void setGlitchDelay(int delay) {
		this.glitchDelay = delay;
	}
	
	public void glitch(int duration) {
		this.glitchDuration = duration;
		glitching = true;
		getGlitchFrame();
		glitchStartTime = System.nanoTime();
//		Logger.logPlayer("Glitching for " + duration + "ms");
	}
	
	public boolean hasGlitchedOnce() {
		return glitchedOnce;
	}
	
	@Override
	public void update() {
		if (glitching) {
			long elapsed = TimeScale.getScaledTime(glitchStartTime);
			long frameElapsed = TimeScale.getScaledTime(glitchFrameTimer);
			if (frameElapsed > glitchDelay) {
				nextGlitchFrame();
			}
			if (elapsed > glitchDuration) {
				stopGlitch();
			}
		}
		else super.update();
	}

	private void nextGlitchFrame() {
		currentGlitchFrame++;
		glitchFrameTimer = System.nanoTime();
		getGlitchFrame();
		if (currentGlitchFrame >= glitchFrames.getNumTextures()) {
			glitchedOnce = true;
			currentGlitchFrame = 0;
		}
	}

	private void getGlitchFrame() {
//		Logger.log("This glitch animation is ");
		int index = RandomUtil.getInt(glitchFrames.getNumTextures());
		glitchFrame = glitchFrames.getFrame(index);
	}

	private void stopGlitch() {
		glitching = false;
		startTime = System.nanoTime();
	}
	
}