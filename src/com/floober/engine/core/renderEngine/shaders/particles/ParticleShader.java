package com.floober.engine.core.renderEngine.shaders.particles;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/particle/particleVertex.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/particle/particlePointFragment.glsl";

	private int location_numRows;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numRows = super.getUniformLocation("numRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "transformationMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
		super.bindAttribute(7, "color");
	}

	// load uniforms
	public void loadNumRows(float numRows) { super.loadFloat(location_numRows, numRows); }

}