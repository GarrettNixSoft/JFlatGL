package com.gnix.jflatgl.core.renderEngine.ppfx;

import com.gnix.jflatgl.core.renderEngine.framebuffers.FrameBuffer;
import org.lwjgl.opengl.GL11;

public class ImageRenderer {

	private FrameBuffer fbo;

	public ImageRenderer(long windowID, int width, int height) {
		this.fbo = new FrameBuffer(windowID, width, height, FrameBuffer.NONE);
	}

	public ImageRenderer() {}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColorTexture();
	}

	protected void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
