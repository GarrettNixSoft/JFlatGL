package com.floober.engine.core.renderEngine.shaders.textures;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TextureOutlineShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "texture/textureVertex.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "texture/textureOutline.glsl";

	private int location_transformationMatrix;
	private int location_textureOffset;
	private int location_stepSize;
	private int location_outlineColor;

	public TextureOutlineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_textureOffset = super.getUniformLocation("textureOffset");
		location_stepSize = super.getUniformLocation("stepSize");
		location_outlineColor = super.getUniformLocation("outlineColor");
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

	public void loadStepSize(Vector2f stepSize) { super.loadVector(location_stepSize, stepSize); }
	public void loadOutlineColor(Vector4f outlineColor) { super.loadVector(location_outlineColor, outlineColor); }

}