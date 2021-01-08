package com.floober.engine.renderEngine.shaders.ppfx;

import com.floober.engine.renderEngine.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram  {

	private static final String VERTEX_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/ppfx/vertexGeneric.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/ppfx/contrastFragment.glsl";

	private int location_contrastChangeAmount;

	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_contrastChangeAmount = super.getUniformLocation("contrast");
	}

	public void loadContrastChangeAmount(float contrastChangeAmount) {
		super.loadFloat(location_contrastChangeAmount, contrastChangeAmount);
	}

}
