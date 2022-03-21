package com.floober.engine.core.renderEngine.shaders.geometry;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class OutlineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/geometry/rectVertex.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/geometry/outlineFragment.glsl";

	private int location_roundRadius;
	private int location_dimensions;
	private int location_lineWidth;
	private int location_color;
	private int location_transformationMatrix;

	public OutlineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_roundRadius = super.getUniformLocation("r");
		location_dimensions = super.getUniformLocation("dimensions");
		location_lineWidth = super.getUniformLocation("lineWidth");
		location_color = super.getUniformLocation("color");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadRoundRadius(float radius) { super.loadFloat(location_roundRadius, radius); }
	public void loadDimensions(Vector2f dimensions) { super.loadVector(location_dimensions, dimensions); }
	public void loadLineWidth(float lineWidth) { super.loadFloat(location_lineWidth, lineWidth); }
	public void loadColor(Vector4f color) {
		super.loadVector(location_color, color);
	}
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

}