package com.floober.engine.renderEngine.renderers;

import com.floober.engine.renderEngine.elements.geometry.*;
import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.shaders.geometry.CircleShader;
import com.floober.engine.renderEngine.shaders.geometry.RectLightShader;
import com.floober.engine.renderEngine.shaders.geometry.RectShader;
import com.floober.engine.util.math.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * The GeometryRenderer handles rendering of geometric elements such as
 * rectangles, circles and lines.
 */
public class GeometryRenderer {

	// quad model
	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private final QuadModel quad;
	private final int lineVAO;
	private final int lineVBO;

	// shaders
	private final RectShader rectShader;
	private final RectLightShader rectLightShader;
	private final CircleShader circleShader;

	// debug
	public static int ELEMENT_COUNT = 0;

	public GeometryRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		lineVAO = ModelLoader.createVAO();
		lineVBO = ModelLoader.createLineVBO();
		rectShader = new RectShader();
		rectLightShader = new RectLightShader();
		circleShader = new CircleShader();
	}

	// RENDER METHODS
	public void renderRectangles(List<RectElement> rectangles, boolean depthWritingEnabled) {

		ELEMENT_COUNT += rectangles.size();

		prepareRectangles(depthWritingEnabled);

		for (RectElement rectElement : rectangles) {

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(rectElement.getRenderPosition(), rectElement.getScale(), rectElement.getRotation());
			rectShader.loadRoundRadius(rectElement.getRoundRadius());
			rectShader.loadDimensions(new Vector2f(rectElement.getHeight(), rectElement.getWidth()));
			rectShader.loadColor(rectElement.getColor());
			rectShader.loadTransformationMatrix(transformationMatrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

		}

		finishRectangles();

	}

	public void renderLightRectangles(List<RectElementLight> elements) {

		ELEMENT_COUNT += elements.size();

		prepareLightRectangles(false);

		for (RectElementLight element : elements) {

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());
			rectLightShader.loadColor(element.getLightColor());
			rectLightShader.loadTransformationMatrix(transformationMatrix);
			rectLightShader.loadAmbientLight(element.getAmbientLight());
			rectLightShader.loadLightIntensity(element.getLightIntensity());
			rectLightShader.loadLightRadius(element.getLightRadius());
			rectLightShader.loadLightInnerRadius(element.getLightInnerRadius());
			rectLightShader.loadLightPosition(element.getLightPosition());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

		}

		finishLightRectangles();

	}

	public void renderCircles(List<CircleElement> circles, boolean depthWritingEnabled) {

		ELEMENT_COUNT += circles.size();

		prepareCircles(depthWritingEnabled);

		for (CircleElement circleElement : circles) {

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(circleElement.getPosition(), circleElement.getScale(), circleElement.getRotation());
			circleShader.loadColor(circleElement.getColor());
			circleShader.loadTransformationMatrix(transformationMatrix);
			circleShader.loadCenter(circleElement.getCenter());
			circleShader.loadInnerRadius(circleElement.getInnerRadius());
			circleShader.loadOuterRadius(circleElement.getOuterRadius());
			circleShader.loadPortion(circleElement.getPortion());
			circleShader.loadSmoothness(circleElement.getSmoothness());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

		}

		finishCircles();

	}

	public void renderLines(List<LineElement> lines, boolean depthWritingEnabled) {

		ELEMENT_COUNT += lines.size();

		prepareRectangles(depthWritingEnabled);

		for (LineElement lineElement : lines) {
			renderLineElement(lineElement);
		}

		finishRectangles();

	}

	public void renderOutlines(List<OutlineElement> outlines, boolean depthWritingEnabled) {

		ELEMENT_COUNT += outlines.size() * 4;

		prepareRectangles(depthWritingEnabled);

		for (OutlineElement outlineElement : outlines) {
			for (LineElement lineElement : outlineElement.getLines()) {
				renderLineElement(lineElement);
			}
		}

		finishRectangles();

	}

	// LINE UTILITY METHOD
	private void renderLineElement(LineElement lineElement) {
		Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(lineElement.getPosition(), lineElement.getScale(), lineElement.getRotation());
		rectShader.loadTransformationMatrix(transformationMatrix);
		rectShader.loadColor(lineElement.getColor());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());
	}

	// PREPARE METHODS
	private void prepareRectangles(boolean depthWritingEnabled) {
		rectShader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(depthWritingEnabled);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
	}

	private void finishRectangles() {
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		rectShader.stop();
	}

	private void prepareLightRectangles(boolean depthWritingEnabled) {
		rectLightShader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(depthWritingEnabled);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
	}

	private void finishLightRectangles() {
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		rectLightShader.stop();
	}

	private void prepareCircles(boolean depthWritingEnabled) {
		circleShader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(depthWritingEnabled);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
	}

	private void finishCircles() {
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		circleShader.stop();
	}

	public void cleanUp() {
		rectShader.cleanUp();
		circleShader.cleanUp();
		ModelLoader.deleteVAO(lineVAO);
		ModelLoader.deleteVBO(lineVBO);
		// other shaders go here
	}

}