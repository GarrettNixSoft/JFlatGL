package com.floober.engine.renderEngine.renderers;

import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.elements.geometry.CircleElement;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import com.floober.engine.renderEngine.shaders.geometry.CircleShader;
import com.floober.engine.renderEngine.shaders.geometry.RectShader;
import com.floober.engine.util.math.MathUtil;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * The GeometryRenderer handles rendering of geometric elements such as
 * rectangles, circles and lines.
 */
public class GeometryRenderer {

	// quad model
	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static final float[] linePositions = {-1, 0, 1, 0};
	private final QuadModel quad;
	private final int lineVAO;
	private final int lineVBO;

	private final FloatBuffer lineBuffer = BufferUtils.createFloatBuffer(4);

	// shaders
	private final RectShader rectShader;
	private final CircleShader circleShader;

	public GeometryRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		lineVAO = ModelLoader.createVAO();
		lineVBO = ModelLoader.createLineVBO();
		rectShader = new RectShader();
		circleShader = new CircleShader();
	}

	// RENDER METHODS
	public void renderRectangles(List<RectElement> rectangles) {

		prepareRectangles();

		for (RectElement rectElement : rectangles) {

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(rectElement.getPosition(), rectElement.getScale());
			rectShader.loadColor(rectElement.getColor());
			rectShader.loadTransformationMatrix(transformationMatrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

		}

		finishRectangles();

	}

	public void renderCircles(List<CircleElement> circles) {

		prepareCircles();

		for (CircleElement circleElement : circles) {

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(circleElement.getPosition(), circleElement.getScale());
			circleShader.loadColor(circleElement.getColor());
			circleShader.loadTransformationMatrix(transformationMatrix);
			circleShader.loadCenter(circleElement.getCenter());
			circleShader.loadInnerRadius(circleElement.getInnerRadius());
			circleShader.loadOuterRadius(circleElement.getOuterRadius());
			circleShader.loadPortion(circleElement.getPortion());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

		}

		finishCircles();

	}

	public void renderLines(List<LineElement> lines) {

		prepareRectangles();

		for (LineElement lineElement : lines) {
			renderLineElement(lineElement);
		}

		finishRectangles();

	}

	public void renderOutlines(List<OutlineElement> outlines) {

		prepareRectangles();

		for (OutlineElement outlineElement : outlines) {
			for (LineElement lineElement : outlineElement.getLines()) {
				renderLineElement(lineElement);
			}
		}

		finishRectangles();

	}

	// LINE UTILITY METHOD
	private void renderLineElement(LineElement lineElement) {
		Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(lineElement.getPosition(), lineElement.getScale());
		rectShader.loadTransformationMatrix(transformationMatrix);
		rectShader.loadColor(lineElement.getColor());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
	}

	// PREPARE METHODS
	private void prepareRectangles() {
		rectShader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void finishRectangles() {
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		rectShader.stop();
	}

	private void prepareCircles() {
		circleShader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void finishCircles() {
		glDisable(GL_BLEND);
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