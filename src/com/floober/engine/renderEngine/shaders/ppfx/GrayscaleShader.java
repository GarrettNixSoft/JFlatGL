package com.floober.engine.renderEngine.shaders.ppfx;

import com.floober.engine.renderEngine.shaders.ShaderProgram;

public class GrayscaleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/ppfx/vertexGeneric.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/ppfx/grayscaleFragment.glsl";

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