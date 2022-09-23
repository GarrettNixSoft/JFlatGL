package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.models.QuadModel;
import com.gnix.jflatgl.core.renderEngine.particles.types.LightParticle;
import com.gnix.jflatgl.core.renderEngine.shaders.particles.ParticleLightShader;
import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.math.MatrixUtils;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public class ParticleLightRenderer {

	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCES = 25000;
	private static final int INSTANCE_DATA_LENGTH = 27; // TRANSFORM (16), COLOR (4), INNER RAD (1), OUTER RAD(1), LIGHT MODE (1), LIGHT COLOR (3), LIGHT INTENSITY (1)

	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private final QuadModel quad;
	private final ParticleLightShader shader;
	private final int vbo;
	private int pointer = 0;

	public ParticleLightRenderer() {
		this.vbo = ModelLoader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = ModelLoader.loadToVAO(VERTICES);
		int vaoID = quad.vaoID();
		ModelLoader.addInstancedAttribute(vaoID, vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);  // Transformation col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);  // Transformation col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);  // Transformation col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // Transformation col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 5, 4, INSTANCE_DATA_LENGTH, 16); // Color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 6, 1, INSTANCE_DATA_LENGTH, 20); // Inner radius
		ModelLoader.addInstancedAttribute(vaoID, vbo, 7, 1, INSTANCE_DATA_LENGTH, 21); // Outer radius
		ModelLoader.addInstancedAttribute(vaoID, vbo, 8, 1, INSTANCE_DATA_LENGTH, 22); // Light mode
		ModelLoader.addInstancedAttribute(vaoID, vbo, 9, 3, INSTANCE_DATA_LENGTH, 23); // Light color
		ModelLoader.addInstancedAttribute(vaoID, vbo, 10, 1, INSTANCE_DATA_LENGTH, 26); // Light intensity
		shader = new ParticleLightShader();
	}

	/**
	 * Render all Light-Emitting Particles.
	 * @param particles All Light-Emitting Particles stored in a List.
	 */
	public void render(List<LightParticle> particles) {

		// prepare for rendering
		prepare();

		// update all particle VBO data
		float[] vboData = new float[particles.size() * INSTANCE_DATA_LENGTH];
		pointer = 0;

		for (LightParticle particle : particles) {
			updateParticleData(particle, vboData);
		}

		// send all the vbo data to the GPU
		ModelLoader.updateVBO(vbo, vboData, buffer);

//		// print out one particle's worth of data to check it
//		if (vboData.length >= INSTANCE_DATA_LENGTH) {
//			for (int i = 0; i < INSTANCE_DATA_LENGTH; i++) {
//				System.out.print(vboData[i] + " ");
//			}
//			System.out.println();
//		}

		// render all particles in this batch in one go!
		glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.vertexCount(), particles.size());

		// finish this render call
		finishRendering();

	}

	private void updateParticleData(LightParticle particle, float[] vboData) {
		Matrix4f matrix = MathUtil.createTransformationMatrix(particle.getScreenPosition(), particle.getScaleVec(), particle.getRotation());
		pointer = MatrixUtils.storeMatrixData(matrix, vboData, pointer);
		vboData[pointer++] = particle.getColor().x();		// Color R
		vboData[pointer++] = particle.getColor().y();		// Color G
		vboData[pointer++] = particle.getColor().z();		// Color B
		vboData[pointer++] = particle.getColor().w();		// Color A
		vboData[pointer++] = particle.getInnerRadius();		// Inner radius
		vboData[pointer++] = particle.getOuterRadius();		// Outer radius
		vboData[pointer++] = particle.getLightMode();		// Light mode
		vboData[pointer++] = particle.getLightColor().x();	// Light color R
		vboData[pointer++] = particle.getLightColor().y();	// Light color G
		vboData[pointer++] = particle.getLightColor().z();	// Light color B
		vboData[pointer++] = particle.getLightIntensity();	// Light intensity
	}

	private void prepare() {
		shader.start();
		glBindVertexArray(quad.vaoID());
		for (int i = 0; i <= 10; ++i) {
			glEnableVertexAttribArray(i);
		}
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glDepthMask(false);
	}

	private void finishRendering() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		for (int i = 0; i <= 10; ++i) {
			glDisableVertexAttribArray(i);
		}
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
