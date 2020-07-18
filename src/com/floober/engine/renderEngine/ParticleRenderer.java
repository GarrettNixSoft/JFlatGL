package com.floober.engine.renderEngine;

import com.floober.engine.models.ModelLoader;
import com.floober.engine.models.QuadModel;
import com.floober.engine.particles.Particle;
import com.floober.engine.particles.ParticleTexture;
import com.floober.engine.shaders.ParticleShader;
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
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

public class ParticleRenderer {

	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCES = 25000;
	private static final int INSTANCE_DATA_LENGTH = 25; // TRNSFM_MTRX (16), TEX_OFF (4), BLEND (1), COLOR (4)

	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private final QuadModel quad;
	private final ParticleShader shader;
	private final int vbo;
	private int pointer = 0;

	public ParticleRenderer() {
		this.vbo = ModelLoader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = ModelLoader.loadToVAO(VERTICES);
		int vaoID = quad.getVaoID();
		ModelLoader.addInstancedAttribute(vaoID, vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);  // Transformation col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);  // Transformation col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);  // Transformation col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // Transformation col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 5, 4, INSTANCE_DATA_LENGTH, 16); // Texture offsets
		ModelLoader.addInstancedAttribute(vaoID, vbo, 6, 1, INSTANCE_DATA_LENGTH, 20); // Blend factor
		ModelLoader.addInstancedAttribute(vaoID, vbo, 7, 4, INSTANCE_DATA_LENGTH, 21); // Color
		shader = new ParticleShader();
	}

	/**
	 * Render all particles.
	 * @param particles A HashMap containing batches of particles using each unique ParticleTexture. Each
	 *                  batch will be rendered in one GPU call using instanced rendering.
	 */
	public void render(Map<ParticleTexture, List<Particle>> particles) {
		prepare();

		for (ParticleTexture particleTexture : particles.keySet()) {

			// bind the texture used for this batch of particles
			bindTexture(particleTexture);

			// get this batch of particles
			List<Particle> particleList = particles.get(particleTexture);

			// sort these particles

			// allocate a float array to store the vbo data, and an index pointer to use when inserting it
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			pointer = 0;

			// for each particle in this batch, add its data to the vbo array
			for (Particle particle : particleList) {
				updateParticleData(particle.getPosition(), particle.getScaleVec(), particle.getRotation(), vboData);
				updateTexCoordInfo(particle, vboData);
			}

			// send all the vbo data to the GPU
			ModelLoader.updateVBO(vbo, vboData, buffer);

			// render all particles in this batch in one go!
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());

		}

		finishRendering();
	}

	private void bindTexture(ParticleTexture particleTexture) {
		if (particleTexture.useAdditiveBlend())
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		else
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, particleTexture.getTextureID());
		shader.loadNumRows(particleTexture.getNumRows());
	}

	private void updateParticleData(Vector3f particlePosition, Vector2f particleScale, float particleRotation, float[] vboData) {
		Matrix4f matrix = MathUtil.createTransformationMatrix(particlePosition, particleScale, particleRotation);
		pointer = MatrixUtils.storeMatrixData(matrix, vboData, pointer);
	}

	private void updateTexCoordInfo(Particle particle, float[] vboData) {
		vboData[pointer++] = particle.getTexOffset1().x();
		vboData[pointer++] = particle.getTexOffset1().y();
		vboData[pointer++] = particle.getTexOffset2().x();
		vboData[pointer++] = particle.getTexOffset2().y();
		vboData[pointer++] = particle.getBlend();
		vboData[pointer++] = particle.getColor().x();
		vboData[pointer++] = particle.getColor().y();
		vboData[pointer++] = particle.getColor().z();
		vboData[pointer++] = particle.getColor().w();
	}

	private void prepare() {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		for (int i = 0; i < 8; ++i) {
			glEnableVertexAttribArray(i);
		}
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glDepthMask(false);
	}

	private void finishRendering() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		for (int i = 0; i < 8; ++i) {
			glDisableVertexAttribArray(i);
		}
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}