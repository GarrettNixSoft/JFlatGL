package com.floober.engine.shaders;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TextureShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/shaders/shadercode/textureVertex";
	private static final String FRAGMENT_FILE = "/com/floober/engine/shaders/shadercode/textureFragment";

	private int location_transformationMatrix;
	private int location_textureOffset;

	public TextureShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_textureOffset = super.getUniformLocation("textureOffset");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

	public void loadTextureOffset(Vector4f textureOffset) {
		super.loadVector(location_textureOffset, textureOffset);
	}

}