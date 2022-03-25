package com.floober.engine.core.renderEngine.shaders.geometry;

import com.floober.engine.core.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CircleShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "geometry/circleVertex.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "geometry/circleFragment.glsl";

	private int location_color;
	private int location_transformationMatrix;
	private int location_center;
	private int location_innerRadius;
	private int location_outerRadius;
	private int location_portion;
	private int location_smoothness;

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
		location_smoothness = super.getUniformLocation("smoothness");
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

	public void loadSmoothness(float smoothness) {
		super.loadFloat(location_smoothness, smoothness);
	}

}