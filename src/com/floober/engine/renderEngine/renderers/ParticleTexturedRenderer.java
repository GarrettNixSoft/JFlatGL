package com.floober.engine.renderEngine.renderers;

import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.particles.ParticleTexture;
import com.floober.engine.renderEngine.particles.types.TexturedParticle;
import com.floober.engine.renderEngine.shaders.particles.ParticleTexturedShader;
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

public class ParticleTexturedRenderer {

	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCES = 25000;
	private static final int INSTANCE_DATA_LENGTH = 21; // TRNSFM_MTRX (16), TEX_OFF (4), ALPHA (1)

	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private final QuadModel quad;
	private final ParticleTexturedShader shader;
	private final int vbo;
	private int pointer = 0;

	public ParticleTexturedRenderer() {
		this.vbo = ModelLoader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = ModelLoader.loadToVAO(VERTICES);
		int vaoID = quad.vaoID();
		ModelLoader.addInstancedAttribute(vaoID, vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);  // Transformation col 1
		ModelLoader.addInstancedAttribute(vaoID, vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);  // Transformation col 2
		ModelLoader.addInstancedAttribute(vaoID, vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);  // Transformation col 3
		ModelLoader.addInstancedAttribute(vaoID, vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // Transformation col 4
		ModelLoader.addInstancedAttribute(vaoID, vbo, 5, 4, INSTANCE_DATA_LENGTH, 16); // Texture offsets
		ModelLoader.addInstancedAttribute(vaoID, vbo, 6, 1, INSTANCE_DATA_LENGTH, 20); // Alpha
		shader = new ParticleTexturedShader();
	}

	/**
	 * Render all textured particles.
	 * @param particles A HashMap containing batches of textured particles using each unique ParticleTexture. Each
	 *                  batch will be rendered in one GPU call using instanced rendering.
	 */
	public void render(Map<ParticleTexture, List<TexturedParticle>> particles) {

		prepare();

		for (ParticleTexture particleTexture : particles.keySet()) {

			// bind the texture used for this batch of particles
			bindTexture(particleTexture);

			// get this batch of particles
			List<TexturedParticle> particleList = particles.get(particleTexture);

			// sort these particles

			// allocate a float array to store the vbo data, and an index pointer to use when inserting it
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			pointer = 0;

			// for each particle in this batch, add its data to the vbo array
			for (TexturedParticle particle : particleList) {
				updateParticleData(particle.getPosition(), particle.getScaleVec(), particle.getRotation(), vboData);
				updateTexCoordInfo(particle, vboData);
			}

			// send all the vbo data to the GPU
			ModelLoader.updateVBO(vbo, vboData, buffer);

			// render all particles in this batch in one go!
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.vertexCount(), particleList.size());

		}

		finishRendering();
	}

	private void bindTexture(ParticleTexture particleTexture) {
		if (particleTexture.useAdditiveBlend())
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		else
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, particleTexture.id());
	}

	private void updateParticleData(Vector3f particlePosition, Vector2f particleScale, float particleRotation, float[] vboData) {
		Matrix4f matrix = MathUtil.createTransformationMatrix(particlePosition, particleScale, particleRotation);
		pointer = MatrixUtils.storeMatrixData(matrix, vboData, pointer);
	}

	private void updateTexCoordInfo(TexturedParticle particle, float[] vboData) {
		vboData[pointer++] = particle.getTextureCoords().x();	// Tex offset 1 x
		vboData[pointer++] = particle.getTextureCoords().y();	// Tex offset 1 y
		vboData[pointer++] = particle.getTextureCoords().z();	// Tex offset 2 x
		vboData[pointer++] = particle.getTextureCoords().w();	// Tex offset 2 y
		vboData[pointer++] = particle.getColor().w();		// Alpha
	}

	private void prepare() {
		shader.start();
		glBindVertexArray(quad.vaoID());
		for (int i = 0; i <= 6; ++i) {
			glEnableVertexAttribArray(i);
		}
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glDepthMask(false);
	}

	private void finishRendering() {
		glDepthMask(true);
		glDisable(GL_BLEND);
		for (int i = 0; i <= 6; ++i) {
			glDisableVertexAttribArray(i);
		}
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}