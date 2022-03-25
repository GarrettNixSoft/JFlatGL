package com.floober.engine.core.renderEngine.shaders.particles;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;

public class ParticleTexturedShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "particle/particleVertexTextured.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "particle/particleFragmentTextured.glsl";

	public ParticleTexturedShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "transformationMatrix");
		super.bindAttribute(5, "textureCoordOffsets");
		super.bindAttribute(6, "alpha");
	}
}