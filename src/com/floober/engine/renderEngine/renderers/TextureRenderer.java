package com.floober.engine.renderEngine.renderers;

import com.floober.engine.display.Display;
import com.floober.engine.renderEngine.lights.LightMaster;
import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.shaders.TextureShader;
import com.floober.engine.renderEngine.textures.Texture;
import com.floober.engine.util.math.MathUtil;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class TextureRenderer {

	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};

	private final QuadModel quad;
	private final TextureShader shader;

	public TextureRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		shader = new TextureShader();
	}

	/**
	 * Render all TextureElements to the scene.
	 * @param textureElements A list containing lists of texture elements that all use one texture per list.
	 */
	public void render(List<TextureElement> textureElements) {

		prepare();

		// send the light information to the shaders
		shader.loadScreenRatio(Display.SCREEN_RATIO);
		shader.loadAmbientLight(LightMaster.getAmbientLight());
		shader.loadLights(LightMaster.getSceneLights());

		for (TextureElement element : textureElements) {

			// bind the current texture
			bindTexture(element.getTexture());

			// bind this element's data
			Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale());
			shader.loadTransformationMatrix(matrix);
			shader.loadTextureOffset(element.getTextureOffset());
			shader.loadDoLighting(element.doLighting());

			// draw the element
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

		}

		finishRendering();

	}

	private void bindTexture(Texture texture) {
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getId());
	}

	/**
	 * Prepare the TextureRenderer for rendering. Activates the Texture Shader,
	 * then enables the VBOs for vertex positions and transformation matrices.
	 */
	private void prepare() {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * Repeat the process of prepare() in reverse to finish this rendering process.
	 */
	private void finishRendering() {
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}