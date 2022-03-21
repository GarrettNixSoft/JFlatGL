package com.floober.engine.core.renderEngine.shaders.ppfx;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;

public class InvertColorShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/ppfx/vertexGeneric.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/ppfx/invertColorFragment.glsl";

	public InvertColorShader() {
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
