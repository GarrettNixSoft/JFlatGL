package com.floober.engine.core.renderEngine.shaders;

public class ParticleTexturedShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/particle/particleVertexTextured.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/particle/particleFragmentTextured.glsl";

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