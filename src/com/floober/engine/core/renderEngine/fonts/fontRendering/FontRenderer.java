package com.floober.engine.core.renderEngine.fonts.fontRendering;

import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.models.ModelLoader;
import com.floober.engine.core.renderEngine.models.QuadModel;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.shaders.FontShader;
import com.floober.engine.core.renderEngine.shaders.StencilShader;
import com.floober.engine.core.util.math.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Set;

import static com.floober.engine.core.renderEngine.display.DisplayManager.primaryWindowID;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class FontRenderer {

	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private final QuadModel quad;

	private final FontShader shader;
	private final StencilShader stencilShader;

	public static int ELEMENT_COUNT = 0;

	public FontRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		shader = new FontShader();
		stencilShader = new StencilShader();
	}

	public void render(Map<FontType, Set<GUIText>> texts) {

		prepare();

		for (FontType fontType : texts.keySet()) {

			ELEMENT_COUNT += texts.get(fontType).size();

			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, fontType.getTextureAtlas());

			for (GUIText text : texts.get(fontType)) {
				renderText(text);
			}
		}

		endRendering();

	}

	private void prepare() {
		shader.start();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(false);
	}

	private void renderText(GUIText text) {
		// TEST
		if (text.isUseStencil()) {
			renderStencil(text);
		}
		// END_TEST
		// fix position
		Vector3f textPos = text.getPosition();
		textPos.z = MasterRenderer.getScreenZ((int) textPos.z);
//		if (!text.getTextString().startsWith("FPS")) Logger.log("Rendering text: " + text.getTextString().substring(0, Math.min(text.getTextString().length(), 16)));
//		Logger.log("RENDERING TEXT AT POS: " + StringConverter.vec3fToString(textPos));
		// send data to shader
		glBindVertexArray(text.getMesh());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		shader.loadColor(text.getColor());
		shader.loadTranslation(textPos);
		shader.loadWidth(text.getWidth());
		shader.loadEdge(text.getEdge());
		shader.loadBorderWidth(text.getBorderWidth());
		shader.loadBorderEdge(text.getBorderEdge());
		shader.loadShadowOffset(text.getShadowOffset());
		shader.loadOutlineColor(text.getOutlineColor());
		// actual render call
		glDrawArrays(GL_TRIANGLES, text.getFirstCharVisible() * 6, text.getNumVisibleChars() * 6);
		// clean up
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		// TEST
		if (text.isUseStencil()) {
//			glStencilMask(0xFF);
//			glStencilFunc(GL_ALWAYS, 1, 0xFF);
			glDisable(GL_STENCIL_TEST);
		}
		// END_TEST
	}

	private void renderStencil(GUIText text) {

		// stop the font shader
		shader.stop();

		// clear the stencil buffer
		glClear(GL_STENCIL_BUFFER_BIT);

		// get whether the depth test is enabled
//		boolean depth = glGetBoolean(GL_DEPTH_TEST);
//		glDisable(GL_DEPTH_TEST);

		glEnable(GL_DEPTH_TEST);

		// set the stencil function to make rendering affect the stencil buffer
//		glDisable(GL_BLEND);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		glStencilFunc(GL_ALWAYS, 1, 0xFF);
		glEnable(GL_STENCIL_TEST);
		glStencilMask(0xFF);

		// prevent the stencil from affecting the color buffer
		glColorMask(false, false, false, false);

		// bind the VAO
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);

		// start the stencil shader and load the transform
		stencilShader.start();

		// get a transformation matrix and send it to the stencil shader
		Vector2f pos = text.getStencilPos();
		Vector2f scale = text.getStencilSize();

		pos = DisplayManager.convertToScreenPos(primaryWindowID, pos);
		scale = DisplayManager.convertToDisplayScale(scale.x, scale.y);
		Vector3f renderPos = new Vector3f(pos, 1);

		Matrix4f transformation = MathUtil.createTransformationMatrix(renderPos, scale);
		stencilShader.loadTransformationMatrix(transformation);

		// render the stencil
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

		// stop the stencil shader
		stencilShader.stop();

		// finish, unbind the VAO
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);

		// disable writing to the stencil buffer
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		glStencilFunc(GL_EQUAL, 1, 0xFF);
		glStencilMask(0x00);

		// re-enable changes to the color buffer
		glColorMask(true, true, true, true);

//		glEnable(GL_BLEND);

//		if (depth) glEnable(GL_DEPTH_TEST);

		// restart the font shader
		shader.start();

	}

	public void renderTextManual(GUIText text) {
		prepare();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, text.getFont().getTextureAtlas());
		renderText(text);
		endRendering();
	}

	private void endRendering() {
		shader.stop();
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(true);
	}

	public void cleanUp(){
		shader.cleanUp();
	}

}