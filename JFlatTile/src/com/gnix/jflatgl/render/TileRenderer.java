package com.gnix.jflatgl.render;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.lights.LightMaster;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.models.QuadModel;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;
import com.gnix.jflatgl.core.renderEngine.renderers.MasterRenderer;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.conversion.Converter;
import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.math.MatrixUtils;
import com.gnix.jflatgl.element.TileElement;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileRenderer extends ElementRenderer {

	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static final int MAX_INSTANCES = 10000; // Up to 10000 tiles on screen at once
	private static final int INSTANCE_DATA_LENGTH = 55;
	// TRANSFORMATION_MATRIX (16), TEX COORDS (4), OVERLAY_TRANSFORMATION_MATRIX (16),
	// DO_COLOR_SWAP (1), R_CHANNEL_COLOR (4), G_CHANNEL_COLOR (4), B_CHANNEL_COLOR (4),
	// A_CHANNEL_COLOR (4), DO_OVERLAY (1), OVERLAY_ALPHA (1)

	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private final QuadModel quad;
	private final TileShader shader;

	private final int vbo;
	private int pointer = 0;

	/**
	 * Create a new TileRenderer, which automatically allocates a VAO and
	 * VBOs for instanced rendering, and generates the Tile Shader.
	 */
	public TileRenderer() {
		this.vbo = ModelLoader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = ModelLoader.loadToVAO(positions, 2);
		int vaoID = quad.vaoID();
		ModelLoader.addInstancedAttribute(vaoID, vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);  // Transformation col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);  // Transformation col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);  // Transformation col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // Transformation col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 5, 4, INSTANCE_DATA_LENGTH, 16); // Tex Coords
		ModelLoader.addInstancedAttribute(vaoID, vbo, 6, 4, INSTANCE_DATA_LENGTH, 20); // Overlay transform col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 7, 4, INSTANCE_DATA_LENGTH, 24); // Overlay transform col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 8, 4, INSTANCE_DATA_LENGTH, 28); // Overlay transform col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 9, 4, INSTANCE_DATA_LENGTH, 32); // Overlay transform col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 10, 2, INSTANCE_DATA_LENGTH, 36); // Do color swap, Do overlay
		ModelLoader.addInstancedAttribute(vaoID, vbo, 11, 4, INSTANCE_DATA_LENGTH, 38); // R channel color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 12, 4, INSTANCE_DATA_LENGTH, 42); // G channel color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 13, 4, INSTANCE_DATA_LENGTH, 46); // B channel color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 14, 4, INSTANCE_DATA_LENGTH, 50); // A channel color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 15, 4, INSTANCE_DATA_LENGTH, 54); // Do overlay
		shader = new TileShader();
	}

	@Override
	public void renderAllElements(List<? extends RenderElement> elements, boolean depthWritingEnabled) {

		// don't attempt to render nothing
		if (elements.isEmpty()) return;

		prepare(depthWritingEnabled);

		// fetch the texture atlas
		TextureAtlas textureAtlas = ((TileElement) elements.get(0)).getTextureAtlas();

		// bind the texture used for this batch
		bindTexture(textureAtlas);

		// render this list
		render(elements);

		finishRendering();

	}

	private void render(List<? extends RenderElement> elementList) {
		// known: all elements in elementList have the same texture atlas (NO LONGER KNOWN 10/7/2022)

		// hashmap to store lists elements with unique overlay textures
		HashMap<TextureComponent, List<TileElement>> overlayTileElements = new HashMap<>();
		int numOverlayElements = 0;

		// allocate a float array to store the vbo data, and an index pointer to use when inserting it
		float[] vboData = new float[elementList.size() * INSTANCE_DATA_LENGTH];
		pointer = 0;

		// for each element in this batch, add its data to the vbo array
		for (RenderElement element : elementList) {
			if (element instanceof TileElement tileElement) {
				if (tileElement.doOverlay()) {
					List<TileElement> overlayList = overlayTileElements.computeIfAbsent(tileElement.getOverlayTexture(), k -> new ArrayList<>());
					overlayList.add(tileElement);
					numOverlayElements++;
				}
				else {
					updateElementData(tileElement, vboData);
				}
			}

		}

		// send all the vbo data to the GPU
		ModelLoader.updateVBO(vbo, vboData, buffer);

		// render all particles in this batch in one go!
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount(), elementList.size() - numOverlayElements);

		// render all overlay tile elements
		renderOverlayElements(overlayTileElements);
	}

	private void renderOverlayElements(HashMap<TextureComponent, List<TileElement>> overlayElements) {

		prepare(false);

		for (TextureComponent overlayTexture : overlayElements.keySet()) {

			// bind the overlay texture
			bindOverlayTexture(overlayTexture);

			// get this batch of tiles
			List<TileElement> tileElementList = overlayElements.get(overlayTexture);

			// render this tile list
			// allocate a float array to store the vbo data, and an index pointer to use when inserting it
			float[] vboData = new float[tileElementList.size() * INSTANCE_DATA_LENGTH];
			pointer = 0;

			// for each element in this batch, add its data to the vbo array
			for (TileElement element : tileElementList) {
				updateElementData(element, vboData);
			}

			// send all the vbo data to the GPU
			ModelLoader.updateVBO(vbo, vboData, buffer);

			// render all particles in this batch in one go!
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.vertexCount(), tileElementList.size());

		}

		finishRendering();

	}

	/**
	 * Bind the TextureAtlas that will be used for this Tile batch.
	 * This will generally be called only once per tilemap, and so
	 * once per frame.
	 * @param textureAtlas The TextureAtlas to bind for the shaders.
	 */
	private void bindTexture(TextureAtlas textureAtlas) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureAtlas.texture().id());
	}

	private void bindOverlayTexture(TextureComponent texture) {
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id());
	}

	/**
	 * Write the element for this tile instance to the float array that will
	 * be passed to the shaders.
	 * @param element The TileElement to store data from.
	 * @param vboData The float array to store the data in.
	 */
	private void updateElementData(TileElement element, float[] vboData) {
		// transformation matrix
		Matrix4f matrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getRotation());
		pointer = MatrixUtils.storeMatrixData(matrix, vboData, pointer);
		// texture offsets
		Vector4f texOffsets = element.getTexOffsets();
		vboData[pointer++] = texOffsets.x;
		vboData[pointer++] = texOffsets.y;
		vboData[pointer++] = texOffsets.z;
		vboData[pointer++] = texOffsets.w;
		// overlay matrix
		Matrix4f overlayMatrix;
		if (element.doOverlay())
			overlayMatrix = MathUtil.createTransformationMatrix(element.getPosition(), element.getScale(), element.getOverlayRotation());
		else
			overlayMatrix = new Matrix4f();
		pointer = MatrixUtils.storeMatrixData(overlayMatrix, vboData, pointer);
		// color swap
		vboData[pointer++] = Converter.booleanToFloat(element.doColorSwap());
		// overlay
		vboData[pointer++] = Converter.booleanToFloat(element.doOverlay());
		// r channel color
		Vector4f rChannelColor = element.getrChannelColor();
		vboData[pointer++] = rChannelColor.x;
		vboData[pointer++] = rChannelColor.y;
		vboData[pointer++] = rChannelColor.z;
		vboData[pointer++] = rChannelColor.w;
		// g channel color
		Vector4f gChannelColor = element.getgChannelColor();
		vboData[pointer++] = gChannelColor.x;
		vboData[pointer++] = gChannelColor.y;
		vboData[pointer++] = gChannelColor.z;
		vboData[pointer++] = gChannelColor.w;
		// b channel color
		Vector4f bChannelColor = element.getbChannelColor();
		vboData[pointer++] = bChannelColor.x;
		vboData[pointer++] = bChannelColor.y;
		vboData[pointer++] = bChannelColor.z;
		vboData[pointer++] = bChannelColor.w;
		// a channel color
		Vector4f aChannelColor = element.getaChannelColor();
		vboData[pointer++] = aChannelColor.x;
		vboData[pointer++] = aChannelColor.y;
		vboData[pointer++] = aChannelColor.z;
		vboData[pointer++] = aChannelColor.w;
		// overlay alpha
		vboData[pointer++] = element.getOverlayAlpha();
	}

	/**
	 * Prepare the TextureRenderer for rendering. Activates the Texture Shader,
	 * then enables the VBOs for vertex positions and transformation matrices.
	 */
	private void prepare(boolean depthWritingEnabled) {
		shader.start();
		GL30.glBindVertexArray(quad.vaoID());
		for (int i = 0; i <= 15; i++)
			GL20.glEnableVertexAttribArray(i);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(depthWritingEnabled);
		// send the light information to the shaders
		shader.loadScreenRatio(MasterRenderer.getTargetWindow().getScreenRatio());
		shader.loadAmbientLight(LightMaster.getAmbientLight());
		shader.loadLights(LightMaster.getSceneLights());
	}

	/**
	 * Repeat the process of prepare() in reverse to finish this rendering process.
	 */
	private void finishRendering() {
		// unbind any overlay textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		// disable all the stuff
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		for (int i = 0; i <= 15; i++)
			GL20.glDisableVertexAttribArray(i);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Delete the Tile Shader.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

}
