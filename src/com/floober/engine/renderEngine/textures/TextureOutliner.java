package com.floober.engine.renderEngine.textures;

import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.renderEngine.framebuffers.FrameBuffers;
import com.floober.engine.renderEngine.renderers.TextureRenderer;
import com.floober.engine.renderEngine.shaders.blur.HorizontalBlurShader;
import com.floober.engine.renderEngine.shaders.blur.VerticalBlurShader;
import com.floober.engine.util.Logger;

public class TextureOutliner {

	private static final HorizontalBlurShader horizontalBlurShader = new HorizontalBlurShader();
	private static final VerticalBlurShader verticalBlurShader = new VerticalBlurShader();

	public static void generateOutlineTexture(TextureElement element) {
		// create a framebuffers to use
		int padding = 24;
		FrameBuffer outlineBuffer = FrameBuffers.createFrameBuffer(element.getTexture().width() + padding,
																	element.getTexture().height() + padding);
		FrameBuffer growBuffer1 = FrameBuffers.createFrameBuffer(element.getTexture().width() + padding,
																element.getTexture().height() + padding);
		FrameBuffer growBuffer2 = FrameBuffers.createFrameBuffer(element.getTexture().width() + padding,
																element.getTexture().height() + padding);
		FrameBuffer horizontalBlurBuffer = FrameBuffers.createFrameBuffer(element.getTexture().width() + padding,
																element.getTexture().height() + padding);
		FrameBuffer verticalBlurBuffer = FrameBuffers.createFrameBuffer(element.getTexture().width() + padding,
																element.getTexture().height() + padding);
		TextureRenderer renderer = new TextureRenderer();
		// make a copy of this element, and center its position in the new buffer
		TextureElement copy = new TextureElement(element);
		copy.setPosition(outlineBuffer.getWidth() / 2f, outlineBuffer.getHeight() / 2f, 0);
		copy.setSize(element.getTexture().width(), element.getTexture().height());
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