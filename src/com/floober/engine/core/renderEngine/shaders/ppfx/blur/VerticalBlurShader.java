package com.floober.engine.core.renderEngine.shaders.ppfx.blur;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "/com/floober/engine/core/renderEngine/shaders/shadercode/blur/verticalBlurVertex.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "/com/floober/engine/core/renderEngine/shaders/shadercode/blur/blurFragment.glsl";

	int location_targetHeight;

	public VerticalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTargetHeight(float targetHeight) {
		super.loadFloat(location_targetHeight, targetHeight);
	}

}