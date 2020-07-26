package com.floober.engine.renderEngine;

import com.floober.engine.models.ModelLoader;
import com.floober.engine.models.QuadModel;
import com.floober.engine.renderEngine.elements.TileElement;
import com.floober.engine.shaders.TileShader;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.util.Logger;
import com.floober.engine.util.math.MathUtil;
import com.floober.engine.util.math.MatrixUtils;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public class TileRenderer {

	private static final float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static final int MAX_INSTANCES = 1000;
	private static final int INSTANCE_DATA_LENGTH = 18; // TRNSFM_MTRX (16), TEX COORDS (2)

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
		int vaoID = quad.getVaoID();
		ModelLoader.addInstancedAttribute(vaoID, vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);  // Transformation col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);  // Transformation col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);  // Transformation col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // Transformation col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 5, 2, INSTANCE_DATA_LENGTH, 16); // Tex Coords
		shader = new TileShader();
	}

	/**
	 * Render all Tile Elements in the HashMap in batches using instanced rendering.
	 * @param textureElements A HashMap containing lists of tile elements that all use one texture per list.
	 */
	public void render(Map<TextureAtlas, List<TileElement>> textureElements) {

		prepare();

		for (TextureAtlas textureAtlas : textureElements.keySet()) {

			// bind the texture used for this batch
			bindTexture(textureAtlas);

			// get this batch of texture elements
			List<TileElement> elementList = textureElements.get(textureAtlas);

			// allocate a float array to store the vbo data, and an index pointer to use when inserting it
			float[] vboData = new float[elementList.size() * INSTANCE_DATA_LENGTH];
			pointer = 0;

			// for each element in this batch, add its data to the vbo array
			for (TileElement element : textureElements.get(textureAtlas)) {
				updateElementData(element.getPosition(), element.getScale(), element.getTexOffsets(), vboData);
			}

			// send all the vbo data to the GPU
			ModelLoader.updateVBO(vbo, vboData, buffer);

			// render all particles in this batch in one go!
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), elementList.size());

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
		glBindTexture(GL_TEXTURE_2D, textureAtlas.getId());
		shader.loadNumRows(textureAtlas.getNumRows());
	}

	/**
	 * Write the element for this tile instance to the float array that will
	 * be passed to the shaders.
	 * @param position The position of this tile instance.
	 * @param scale The scale of this tile instance.
	 * @param texOffsets The texture offsets of this tile instance.
	 * @param vboData The float array to store the data in.
	 */
	private void updateElementData(Vector3f position, Vector2f scale, Vector2f texOffsets, float[] vboData) {
		Matrix4f matrix = MathUtil.createTransformationMatrix(position, scale, 0);
		pointer = MatrixUtils.storeMatrixData(matrix, vboData, pointer);
		vboData[pointer++] = texOffsets.x;
		vboData[pointer++] = texOffsets.y;
//		Logger.log("Texture offsets: (" + texOffsets.x + ", " + texOffsets.y + ")");
	}

	/**
	 * Prepare the TextureRenderer for rendering. Activates the Texture Shader,
	 * then enables the VBOs for vertex positions and transformation matrices.
	 */
	private void prepare() {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
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
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
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