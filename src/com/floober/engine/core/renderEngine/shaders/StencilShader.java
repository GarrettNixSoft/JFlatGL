package com.floober.engine.core.renderEngine.shaders;

import org.joml.Matrix4f;

public class StencilShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "genericVertex.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "stencilFragment.glsl";

	private int location_transformationMatrix;

	public StencilShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

}
