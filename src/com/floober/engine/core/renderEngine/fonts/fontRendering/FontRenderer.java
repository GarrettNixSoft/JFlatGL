package com.floober.engine.core.renderEngine.fonts.fontRendering;

import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.shaders.FontShader;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.conversion.StringConverter;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class FontRenderer {

	private final FontShader shader;

	public static int ELEMENT_COUNT = 0;

	public FontRenderer() {
		shader = new FontShader();
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
//		if (text.isUseStencil()) {
//			glEnable(GL_STENCIL_TEST);
//			glStencilFunc(GL_EQUAL, 1, 0xFF);
//		}
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
		glDrawArrays(GL_TRIANGLES, text.getFirstCharVisible() * 6, text.getNumVisibleChars() * 6);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
//		if (text.isUseStencil()) {
//			glDisable(GL_STENCIL_TEST);
//			glStencilFunc(GL_EQUAL, 1, 0x00);
//		}
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