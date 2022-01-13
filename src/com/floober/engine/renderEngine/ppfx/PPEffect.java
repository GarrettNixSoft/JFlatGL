package com.floober.engine.renderEngine.ppfx;

import com.floober.engine.renderEngine.shaders.ShaderProgram;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public abstract class PPEffect {

	protected long windowID;
	protected ImageRenderer renderer;
	protected ShaderProgram shader;

	private boolean enabled;

	/**
	 * Create a Post Processing effect that will render
	 * its image to the screen.
	 */
	public PPEffect(long windowID) {
		this.windowID = windowID;
		renderer = new ImageRenderer();
	}

	/**
	 * Create a Post Processing effect that will render
	 * its image to a FrameBuffer.
	 * @param width the width of the frame buffer
	 * @param height the height of the frame buffer
	 */
	public PPEffect(long windowID, int width, int height) {
		renderer = new ImageRenderer(windowID, width, height);
	}

	/**
	 * Check if this Post Processing effect has been enabled.
	 * @return true if enabled, false if disabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enable this Post Processing stage.
	 */
	public void enable() {
		this.enabled = true;
	}

	/**
	 * Disable this Post Processing stage.
	 */
	public void disable() {
		this.enabled = false;
	}

	/**
	 * The init() method should be used to load any
	 * uniform values needed for this effect's shader.
	 */
	public abstract void init();

	/**
	 * Render the scene with this Post Processing effect
	 * applied.
	 * @param texture the texture value of the current scene
	 */
	public void render(int texture) {
		shader.start();
		init();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}

	public int getResult() {
		return renderer.getOutputTexture();
	}

	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

}