package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.*;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.models.QuadModel;
import com.gnix.jflatgl.core.renderEngine.shaders.geometry.CircleShader;
import com.gnix.jflatgl.core.renderEngine.shaders.geometry.OutlineShader;
import com.gnix.jflatgl.core.renderEngine.shaders.geometry.RectLightShader;
import com.gnix.jflatgl.core.renderEngine.shaders.geometry.RectShader;
import com.gnix.jflatgl.core.renderEngine.util.Stencil;
import com.gnix.jflatgl.core.splash.SplashScreen;
import com.gnix.jflatgl.core.util.math.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * The GeometryRenderer handles rendering of geometric elements such as
 * rectangles, circles and lines.
 */
public class GeometryRenderer extends ElementRenderer {

	// quad model
	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private final QuadModel quad;
	private final int lineVAO;
	private final int lineVBO;

	// shaders
	private final RectShader rectShader;
	private final RectLightShader rectLightShader;
	private final CircleShader circleShader;
	private final OutlineShader outlineShader;

	// debug
	public static int ELEMENT_COUNT = 0;

	public GeometryRenderer() {
		quad = ModelLoader.loadToVAO(positions);
		lineVAO = ModelLoader.createVAO();
		lineVBO = ModelLoader.createLineVBO();
		rectShader = new RectShader();
		rectLightShader = new RectLightShader();
		circleShader = new CircleShader();
		outlineShader = new OutlineShader();
	}

	// RENDER METHODS
	@Override
	public void renderAllElements(List<? extends RenderElement> elements, boolean depthWritingEnabled) {
		// create lists for sorting elements
		List<RectElement> rectElements = new ArrayList<>();
		List<RectElementLight> rectLightElements = new ArrayList<>();
		List<CircleElement> circleElements = new ArrayList<>();
		List<LineElement> lineElements = new ArrayList<>();
		List<OutlineElement> outlineElements = new ArrayList<>();
		// sort elements into the lists
		for (RenderElement element : elements) {
			switch (element) {
				case RectElementLight rectLight -> rectLightElements.add(rectLight);
				case RectElement rect -> rectElements.add(rect);
				case CircleElement circle -> circleElements.add(circle);
				case LineElement line -> lineElements.add(line);
				case OutlineElement outline -> outlineElements.add(outline);
				default -> {}
			}
		}
		// render everything!
		renderRectangles(rectElements, depthWritingEnabled);
		if (!depthWritingEnabled) renderLightRectangles(rectLightElements);
		renderCircles(circleElements, depthWritingEnabled);
		renderLines(lineElements, depthWritingEnabled);
		renderOutlines(outlineElements, depthWritingEnabled);
	}

	public void renderRectangles(List<RectElement> rectangles, boolean depthWritingEnabled) {

		ELEMENT_COUNT += rectangles.size();

		prepareRectangles(depthWritingEnabled);

		for (RectElement element : rectangles) {

			if (element.isStencilWrite()) {
				Stencil.enableStencilWrite();
			}

			Matrix4f transformationMatrix = SplashScreen.SPLASH_RENDER ?
												MathUtil.createTransformationMatrix(SplashScreen.splashWindow, element.getRenderPosition(), element.getScale(), element.getRotation()) :
												MathUtil.createTransformationMatrix(element.getRenderPosition(), element.getScale(), element.getRotation());
			rectShader.loadRoundRadius(element.getRoundRadius());
			rectShader.loadRoundMode(element.getRoundingMode());
			rectShader.loadDimensions(new Vector2f(element.getHeight(), element.getWidth()));
			rectShader.loadColor(element.getColor());
			rectShader.loadTransformationMatrix(transformationMatrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

			if (element.isStencilWrite()) {
				Stencil.disableStencilWrite();
			}

		}

		finishRectangles();

	}

	public void renderLightRectangles(List<RectElementLight> elements) {

		ELEMENT_COUNT += elements.size();

		prepareLightRectangles(false);

		for (RectElementLight element : elements) {

			if (element.isStencilWrite()) {
				Stencil.enableStencilWrite();
			}
			else {
				Stencil.disableStencilWrite();
			}

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

		for (CircleElement element : circles) {

			if (element.isStencilWrite()) {
				Stencil.enableStencilWrite();
			}
			else {
				Stencil.disableStencilWrite();
			}

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());
			circleShader.loadColor(element.getColor());
			circleShader.loadTransformationMatrix(transformationMatrix);
			circleShader.loadInnerRadius(element.getInnerRadius());
//			Logger.log("Inner radius = " + circleElement.getInnerRadius());
			circleShader.loadOuterRadius(element.getOuterRadius());
			circleShader.loadPortion(element.getPortion());
			circleShader.loadSmoothness(element.getSmoothness());
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

		ELEMENT_COUNT += outlines.size();

		prepareOutlines(depthWritingEnabled);

		for (OutlineElement element : outlines) {

			if (element.isStencilWrite()) {
				Stencil.enableStencilWrite();
			}
			else {
				Stencil.disableStencilWrite();
			}

			Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(element.getRenderPosition(), element.getScale(), element.getRotation());
			outlineShader.loadRoundRadius(element.getRoundRadius());
			outlineShader.loadRoundMode(element.getRoundingMode());
			outlineShader.loadDimensions(new Vector2f(element.getHeight(), element.getWidth()));
			outlineShader.loadColor(element.getColor());
			outlineShader.loadLineWidth(element.getLineWidth() / element.getWidth());
			outlineShader.loadTransformationMatrix(transformationMatrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.vertexCount());

		}

		finishOutlines();

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

	private void prepareOutlines(boolean depthWritingEnabled) {
		outlineShader.start();
		glBindVertexArray(quad.vaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glDepthMask(depthWritingEnabled);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
	}

	private void finishOutlines() {
		glDisable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		outlineShader.stop();
	}

	public void cleanUp() {
		rectShader.cleanUp();
		circleShader.cleanUp();
		ModelLoader.deleteVAO(lineVAO);
		ModelLoader.deleteVBO(lineVBO);
		// other shaders go here
	}

}
