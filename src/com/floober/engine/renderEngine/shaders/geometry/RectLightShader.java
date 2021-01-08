package com.floober.engine.renderEngine.shaders.geometry;

import com.floober.engine.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RectLightShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/geometry/rectVertex.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/renderEngine/shaders/shadercode/geometry/rectLightFragment.glsl";

	private int location_color;
	private int location_transformationMatrix;
	private int location_ambientLight;
	private int location_lightIntensity;
	private int location_lightRadius;
	private int location_lightInnerRadius;
	private int location_lightPosition;

	public RectLightShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("lightColor");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_ambientLight = super.getUniformLocation("ambientLight");
		location_lightIntensity = super.getUniformLocation("lightIntensity");
		location_lightRadius = super.getUniformLocation("lightRadius");
		location_lightInnerRadius = super.getUniformLocation("lightInnerRadius");
		location_lightPosition = super.getUniformLocation("lightPosition");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadColor(Vector3f color) {
		super.loadVector(location_color, color);
	}
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	public void loadAmbientLight(float ambientLight) { super.loadFloat(location_ambientLight, ambientLight); }
	public void loadLightIntensity(float lightIntensity) { super.loadFloat(location_lightIntensity, lightIntensity); }
	public void loadLightRadius(float lightRadius) { super.loadFloat(location_lightRadius, lightRadius); }
	public void loadLightInnerRadius(float lightInnerRadius) { super.loadFloat(location_lightInnerRadius, lightInnerRadius); }
	public void loadLightPosition(Vector2f lightPosition) {
		super.loadVector(location_lightPosition, lightPosition);
	}

}