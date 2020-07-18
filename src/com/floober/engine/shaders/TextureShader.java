package com.floober.engine.shaders;

import org.joml.Matrix4f;

public class TextureShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/shaders/shadercode/textureVertex";
	private static final String FRAGMENT_FILE = "/com/floober/engine/shaders/shadercode/textureFragment";

	private int location_transformationMatrix;

	public TextureShader() {
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