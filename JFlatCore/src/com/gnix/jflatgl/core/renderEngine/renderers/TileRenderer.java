package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.renderEngine.elements.tile.TileElement;
import com.gnix.jflatgl.core.renderEngine.lights.LightMaster;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.models.QuadModel;
import com.gnix.jflatgl.core.renderEngine.shaders.textures.TileShader;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.util.conversion.Converter;
import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.math.MatrixUtils;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public class TileRenderer {

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

	/**
	 * Render all Tile Elements in the HashMap in batches using instanced rendering.
	 * @param tileElements A HashMap containing lists of tile elements that all use one texture per list.
	 */
	public void render(Map<TextureAtlas, List<TileElement>> tileElements, boolean depthWritingEnabled) {

		prepare(depthWritingEnabled);

		for (TextureAtlas textureAtlas : tileElements.keySet()) {

			// bind the texture used for this batch
			bindTexture(textureAtlas);

			// get this batch of texture elements
			List<TileElement> elementList = tileElements.get(textureAtlas);

			// render this list
			render(elementList);

		}

		finishRendering();

	}

	private void render(List<TileElement> elementList) {
		// known: all elements in elementList have the same texture atlas

		// hashmap to store lists elements with unique overlay textures
		HashMap<TextureComponent, List<TileElement>> overlayTileElements = new HashMap<>();
		int numOverlayElements = 0;

		// allocate a float array to store the vbo data, and an index pointer to use when inserting it
		float[] vboData = new float[elementList.size() * INSTANCE_DATA_LENGTH];
		pointer = 0;

		// for each element in this batch, add its data to the vbo array
		for (TileElement element : elementList) {
			if (element.doOverlay()) {
				List<TileElement> overlayList = overlayTileElements.computeIfAbsent(element.getOverlayTexture(), k -> new ArrayList<>());
				overlayList.add(element);
				numOverlayElements++;
			}
			else {
				updateElementData(element, vboData);
			}
		}

		// send all the vbo data to the GPU
		ModelLoader.updateVBO(vbo, vboData, buffer);

		// render all particles in this batch in one go!
		glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.vertexCount(), elementList.size() - numOverlayElements);

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
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.vertexCount(), tileElementList.size());

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
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureAtlas.texture().id());
	}

	private void bindOverlayTexture(TextureComponent texture) {
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texture.id());
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
		glBindVertexArray(quad.vaoID());
		for (int i = 0; i <= 15; i++)
			glEnableVertexAttribArray(i);
		glEnable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(depthWritingEnabled);
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
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, 0);
		// disable all the stuff
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		for (int i = 0; i <= 15; i++)
			glDisableVertexAttribArray(i);
		glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Delete the Tile Shader.
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

}
