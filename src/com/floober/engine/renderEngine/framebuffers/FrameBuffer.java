package com.floober.engine.renderEngine.framebuffers;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.display.Window;
import com.floober.engine.renderEngine.textures.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

	public static final int NONE = 0;
	public static final int DEPTH_TEXTURE = 1;
	public static final int DEPTH_RENDER_BUFFER = 2;

	private final Window window;
	private final int width;
	private final int height;
	private int bufferID;

	private int colorTexture;
	private int depthTexture;

	private int depthBuffer;
	private int colorBuffer;

	// flag if the buffer has been deleted
	private boolean deleted = false;

	public FrameBuffer(Window window, int width, int height, int depthBufferType) {
		this.window = window;
		this.width = width;
		this.height = height;
		initializeFrameBuffer(depthBufferType);
	}

	public FrameBuffer(long windowID, int width, int height, int depthBufferType) {
		this.window = DisplayManager.getWindow(windowID);
		this.width = width;
		this.height = height;
		initializeFrameBuffer(depthBufferType);
	}

	// GETTERS
	public int getBufferID() { return bufferID; }
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public boolean isDeleted() { return deleted; }

	/**
	 * @return The ID of the texture containing the colour buffer of the FBO.
	 */
	public int getColorTexture() {
		return colorTexture;
	}

	/**
	 * @return The texture containing the FBO's depth buffer.
	 */
	public int getDepthTexture() {
		return depthTexture;
	}

	public Texture getColorBufferAsTexture() {
		return new Texture(colorTexture, width, height);
	}

	// USAGE

	/**
	 * Binds the frame buffer, setting it as the current render target. Anything
	 * rendered after this will be rendered to this FBO, and not to the screen.
	 */
	public void bindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, bufferID);
//		glfwMakeContextCurrent(window.getWindowID());
//		window.setViewport(0, 0, width, height);
//		Logger.log("The framebuffer is [" + width + " x " + height + "]");
//		window.setViewport();
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Unbinds the frame buffer, setting the default frame buffer as the current
	 * render target. Anything rendered after this will be rendered to the
	 * screen, and not this FBO.
	 */
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		window.setViewport();
//		MasterRenderer.currentRenderTarget.getTargetWindow().setViewport();
	}

	/**
	 * Binds the current FBO to be read from (not used in tutorial 43).
	 */
	public void bindToRead() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, bufferID);
		GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	// CREATION

	/**
	 * Creates the FBO along with a colour buffer texture attachment, and
	 * possibly a depth buffer.
	 *
	 * @param type
	 *            - the type of depth buffer attachment to be attached to the
	 *            FBO.
	 */
	private void initializeFrameBuffer(int type) {
		createFrameBuffer();
		createTextureAttachment();
		if (type == DEPTH_RENDER_BUFFER) {
			createDepthBufferAttachment();
		} else if (type == DEPTH_TEXTURE) {
			createDepthTextureAttachment();
		}
		unbindFrameBuffer();
	}

	/**
	 * Creates a new frame buffer object and sets the buffer to which drawing
	 * will occur - colour attachment 0. This is the attachment where the colour
	 * buffer texture is.
	 */
	private void createFrameBuffer() {
		bufferID = GL30.glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, bufferID);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
	}

	/**
	 * Creates a texture and sets it as the colour buffer attachment for this
	 * FBO.
	 */
	private void createTextureAttachment() {
		colorTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, colorTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTexture, 0);
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a texture, which can later
	 * be sampled.
	 */
	private void createDepthTextureAttachment() {
		depthTexture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, depthTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, width, height, 0, GL_DEPTH_COMPONENT,
				GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		GL30.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);
	}

	/**
	 * Adds a depth buffer to the FBO in the form of a render buffer. This can't
	 * be used for sampling in the shaders.
	 */
	private void createDepthBufferAttachment() {
		depthBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer);
	}

	// DELETION

	/**
	 * Deletes the frame buffer and its attachments when the game closes.
	 */
	public void cleanUp() {
		if (!deleted) glDeleteFramebuffers(bufferID);
		glDeleteTextures(colorTexture);
		glDeleteTextures(depthTexture);
		glDeleteRenderbuffers(depthBuffer);
		glDeleteRenderbuffers(colorBuffer);
	}

	/**
	 * Calls the cleanUp() method. Semantically, use this method
	 * when deleting a framebuffer on the fly, rather than when
	 * deleting it during the clean-up phase on close.
	 */
	public void delete() {
		cleanUp();
	}

	/**
	 * Delete the buffer, but not any of its associated textures.
	 */
	public void deleteButPreserveTextures() {
		// only delete buffer
		glDeleteFramebuffers(bufferID);
		deleted = true;
	}

	/**
	 * Delete the lingering textures that still exist in this buffer
	 * during the clean-up phase.
	 */
	public void deletePreservedTextures() {
		glDeleteTextures(colorTexture);
		glDeleteTextures(depthTexture);
		glDeleteRenderbuffers(depthBuffer);
		glDeleteRenderbuffers(colorBuffer);
	}

}
