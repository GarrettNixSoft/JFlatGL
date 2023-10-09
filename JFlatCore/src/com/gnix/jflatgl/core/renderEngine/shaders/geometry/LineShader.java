package com.gnix.jflatgl.core.renderEngine.shaders.geometry;

import com.gnix.jflatgl.core.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class LineShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "geometry/lineVertex.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "geometry/lineFragment.glsl";

	private int location_color;
	//private int location_transformationMatrix;

	public LineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		//location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadColor(Vector4f color) {
		super.loadVector(location_color, color);
	}

//	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
//		super.loadMatrix(location_transformationMatrix, transformationMatrix);
//	}
}
