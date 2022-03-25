package com.floober.engine.core.renderEngine.shaders.ppfx;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;

public class GrayscaleShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "ppfx/vertexGeneric.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "ppfx/grayscaleFragment.glsl";

	public GrayscaleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		// none
	}
}