package com.floober.engine.renderEngine.renderers;

import com.floober.engine.assets.loaders.ImageLoader;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.framebuffers.FrameBuffer;
import com.floober.engine.renderEngine.lights.LightMaster;
import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.shaders.ShaderProgram;
import com.floober.engine.renderEngine.shaders.blur.HorizontalBlurShader;
import com.floober.engine.renderEngine.shaders.blur.VerticalBlurShader;
import com.floober.engine.renderEngine.shaders.textures.TextureOutlineGrowShader;
import com.floober.engine.renderEngine.shaders.textures.TextureOutlineShader;
import com.floober.engine.renderEngine.shaders.textures.TextureShader;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.math.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TextureRenderer {

	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};

	// shaders
	private final TextureShader shader;
	private final TextureOutlineShader outlineShader;
	private final TextureOutlineGrowShader outlineGrowShader;

	// assets to provide to shaders
	private final QuadModel quad;
	private final Texture glitchNoise;

	// debug
	public static int ELEMENT_COUNT = 0;

	public TextureRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		shader = new TextureShader();
		outlineShader = new TextureOutlineShader();
		outlineGrowShader = new TextureOutlineGrowShader();
		initShader();
		glitchNoise = ImageLoader.loadTexture("textures/noise/glitch_noise.png", GL_REPEAT);
	}

	private void initShader() {
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
	}

	/**
	 * Render all TextureElements to the scene.
	 * @param textureElements A list containing lists of texture elements that all use one texture per list.
	 */
	public void render(List<TextureElement> textureElements, boolean depthWritingEnabled) {

		ELEMENT_COUNT += textureElements.size();

		prepare(depthWritingEnabled);

		for (TextureElement element : textureElements) {

			// bind this element's data
			loadTextureUniforms(element);

//			Logger.log("Element position: " + element.getPosition());

			Matrix4f mat = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());

			// if outline is on, render it under the element
			if (element.doOutline()) {
				bindTexture(element.getOutlineTexture());
				Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getOutlineScale(), element.getRotation());
				shader.loadTransformationMatrix(matrix);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
			}

			shader.loadTransformationMatrix(mat);

			// bind the current texture
			bindTexture(element.getRawTexture());

			// draw the element
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

			// if outline is on, do another call
//			if (element.doOutline()) {
//				bindTexture(element.getOutlineTexture());
//				Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getOutlineScale(), element.getRotation());
//				shader.loadTransformationMatrix(matrix);
//				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
//			}

		}

		finish();

	}

	/**
	 * Render a texture's outline.
	 * @param element The element to draw the outline of.
	 */
	public void renderTextureOutline(TextureElement element) {
		if (element.doOutline()) {
			prepareOutline(outlineShader);
			bindTexture(element.getTextureComponent().texture());
			loadOutlineUniforms(element);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
			finishOutline(outlineShader);
		}
	}

	public FrameBuffer growTextureOutline(TextureElement element, int outlineTexture, int passes, FrameBuffer[] buffers) {
		if (element.doOutline()) {
			FrameBuffer currentBuffer = buffers[0];
			prepareOutline(outlineGrowShader);
			bindTexture(outlineTexture);
			loadOutlineGrowUniforms(element);
			for (int i = 0; i < passes; i++) {
				if (i > 0) {
					bindTexture(currentBuffer.getColorTexture());
					currentBuffer = buffers[i % 2];
				}
				currentBuffer.bindFrameBuffer();
				glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
			}
			finishOutline(outlineGrowShader);
			return currentBuffer;
		}
		else return null; // maybe return a 1x1 buffer to be safe?
	}

	/**
	 * Render a texture raw, with no shader parameters set.
	 * @param texture The texture to render.
	 * @param position The position to render at.
	 * @param scale The scale of the texture in the current buffer.
	 */
	public void rawRender(Texture texture, Vector3f position, Vector2f scale) {
		prepare(false);
		bindTexture(texture);
		shader.loadTransformationMatrix(MathUtil.createTransformationMatrix(position, scale, 0));
		shader.loadTextureAlpha(1);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
		finish();
	}

	public void blurRender(int texture, FrameBuffer horizontalBlurBuffer, HorizontalBlurShader horizontalShader, FrameBuffer verticalBlurBuffer, VerticalBlurShader verticalShader) {
		prepare(false);
		shader.stop();
		bindTexture(texture);
		horizontalBlurBuffer.bindFrameBuffer();
		horizontalShader.start();
		horizontalShader.loadTargetWidth(horizontalBlurBuffer.getWidth());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
		horizontalShader.stop();
		bindTexture(horizontalBlurBuffer.getColorTexture());
		verticalBlurBuffer.bindFrameBuffer();
		verticalShader.start();
		verticalShader.loadTargetHeight(verticalBlurBuffer.getHeight());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
		verticalShader.stop();
		finish();
	}

	private void bindTexture(Texture texture) {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.id());
	}

	private void bindTexture(int texture) {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	private void loadTextureUniforms(TextureElement element) {
		// standard texture data
		Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());
		shader.loadTransformationMatrix(matrix);
		shader.loadTextureOffset(element.getTextureComponentOffset());
		shader.loadTextureAlpha(element.getTextureComponent().getAlpha());
		// color swap
		shader.loadDoColorSwap(element.doColorSwap());
		shader.loadrChannelColor(element.getrChannelColor());
		shader.loadgChannelColor(element.getgChannelColor());
		shader.loadbChannelColor(element.getbChannelColor());
		shader.loadaChannelColor(element.getaChannelColor());
		// lighting data
		shader.loadDoLighting(element.doLighting());
		// color effect
		shader.loadDoColor(element.doColor());
		shader.loadColor(element.getColor());
		shader.loadMix(element.getMix());
		// glitch effect
		shader.loadDoGlitch(element.doGlitch());
		shader.loadTime(DisplayManager.getGameTime());
		shader.loadAmplitude(element.getGlitchAmplitude());
		shader.loadSpeed(element.getGlitchSpeed());
		// fade effect
		shader.loadDoFade(element.doFade());
		shader.loadFadeValues(element.getFadeValues());
		shader.loadFadeOffsets(element.getFadeOffsets());
		shader.loadFadeDirection(element.getFadeDirection());
	}

	private void loadOutlineUniforms(TextureElement element) {
		Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());
		outlineShader.loadTransformationMatrix(matrix);
		outlineShader.loadTextureOffset(element.getTextureComponentOffset());
		outlineShader.loadStepSize(element.stepSize());
		outlineShader.loadOutlineColor(element.outlineColor());
	}

	private void loadOutlineGrowUniforms(TextureElement element) {
		outlineGrowShader.loadTextureOffset(element.getTextureComponentOffset());
		outlineGrowShader.loadStepSize(element.stepUnit());
		outlineGrowShader.loadOutlineColor(element.outlineColor());
	}

	/**
	 * Prepare the TextureRenderer for rendering. Activates the Texture Shader,
	 * then enables the VBOs for vertex positions and transformation matrices.
	 */
	private void prepare(boolean depthWritingEnabled) {
		shader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(depthWritingEnabled);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		// send the light information to the shaders
		shader.loadScreenRatio(MasterRenderer.getTargetWindow().getScreenRatio());
		shader.loadAmbientLight(LightMaster.getAmbientLight());
		shader.loadLights(LightMaster.getSceneLights());
		// bind the glitch texture
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, glitchNoise.id());
	}

	/**
	 * Repeat the process of prepare() in reverse to finish this rendering process.
	 */
	private void finish() {
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	private void prepareOutline(ShaderProgram shader) {
		shader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(false);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
	}

	private void finishOutline(ShaderProgram shader) {
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
		outlineShader.cleanUp();
	}

}