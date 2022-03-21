package com.floober.engine.core.renderEngine.textures;

import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.core.renderEngine.framebuffers.FrameBuffers;
import com.floober.engine.core.renderEngine.renderers.TextureRenderer;
import com.floober.engine.core.renderEngine.shaders.blur.HorizontalBlurShader;
import com.floober.engine.core.renderEngine.shaders.blur.VerticalBlurShader;
import com.floober.engine.core.util.Logger;

public class TextureOutliner {

	private static final HorizontalBlurShader horizontalBlurShader = new HorizontalBlurShader();
	private static final VerticalBlurShader verticalBlurShader = new VerticalBlurShader();

	public static void generateOutlineTexture(long windowID, TextureElement element) {
		// create a framebuffers to use
		TextureComponent texture = element.getTextureComponent();
		int padding = 24;
		FrameBuffer outlineBuffer = FrameBuffers.createFrameBuffer(windowID, texture.width() + padding,
																	texture.height() + padding);
		FrameBuffer growBuffer1 = FrameBuffers.createFrameBuffer(windowID, texture.width() + padding,
																texture.height() + padding);
		FrameBuffer growBuffer2 = FrameBuffers.createFrameBuffer(windowID, texture.width() + padding,
																texture.height() + padding);
		FrameBuffer horizontalBlurBuffer = FrameBuffers.createFrameBuffer(windowID, texture.width() + padding,
																texture.height() + padding);
		FrameBuffer verticalBlurBuffer = FrameBuffers.createFrameBuffer(windowID, texture.width() + padding,
																texture.height() + padding);
		TextureRenderer renderer = new TextureRenderer();
		// make a copy of this element, and center its position in the new buffer
		TextureElement copy = new TextureElement(element);
		copy.setPosition(outlineBuffer.getWidth() / 2f, outlineBuffer.getHeight() / 2f, 0);
		copy.setSize(texture.width(), texture.height());
		copy.setCentered(true);
		copy.transform(outlineBuffer);
		Logger.log("Copy element transformed. Location: " + copy.getPosition() + "; Size: " + copy.getScale());
		// bind the outline buffer
		outlineBuffer.bindFrameBuffer();
		// render the outline
		renderer.renderTextureOutline(copy);
		// expand the outline
		FrameBuffer next = renderer.growTextureOutline(copy, outlineBuffer.getColorTexture(), 6, new FrameBuffer[] {growBuffer1, growBuffer2});
		// render the blurred outline
		renderer.blurRender(next.getColorTexture(), horizontalBlurBuffer, horizontalBlurShader, verticalBlurBuffer, verticalBlurShader);
		// unbind buffers
		next.unbindFrameBuffer();
		// delete the buffers but keep the blur texture
		element.setOutlineTexture(verticalBlurBuffer.getColorBufferAsTexture());
		outlineBuffer.delete(); // delete unnecessary buffers, keep the texture generated
		growBuffer1.delete();
		growBuffer2.delete();
		horizontalBlurBuffer.delete();
		verticalBlurBuffer.deleteButPreserveTextures();
	}

	public static void cleanUp() {
		horizontalBlurShader.cleanUp();
		verticalBlurShader.cleanUp();
	}

}