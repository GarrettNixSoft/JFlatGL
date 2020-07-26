package com.floober.engine.shaders.geometry;

import com.floober.engine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CircleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/shaders/shadercode/geometry/circleVertex";
	private static final String FRAGMENT_FILE = "/com/floober/engine/shaders/shadercode/geometry/circleFragment";

	private int location_color;
	private int location_transformationMatrix;
	private int location_center;
	private int location_innerRadius;
	private int location_outerRadius;
	private int location_portion;

	public CircleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_center = super.getUniformLocation("center");
		location_innerRadius = super.getUniformLocation("innerRadius");
		location_outerRadius = super.getUniformLocation("outerRadius");
		location_portion = super.getUniformLocation("portion");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadColor(Vector4f color) {
		super.loadVector(location_color, color);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

	public void loadCenter(Vector2f center) {
		super.loadVector(location_center, center);
	}

	public void loadInnerRadius(float innerRadius) {
		super.loadFloat(location_innerRadius, innerRadius);
	}

	public void loadOuterRadius(float outerRadius) {
		super.loadFloat(location_outerRadius, outerRadius);
	}

	public void loadPortion(Vector2f portion) {
		super.loadVector(location_portion, portion);
	}

}