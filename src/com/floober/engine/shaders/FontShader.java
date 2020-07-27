package com.floober.engine.shaders;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/com/floober/engine/shaders/shadercode/fontVertex.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/shaders/shadercode/fontFragment.glsl";

	private int location_color;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_shadowOffset;
	private int location_outlineColor;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");
		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_borderWidth = super.getUniformLocation("borderWidth");
		location_borderEdge = super.getUniformLocation("borderEdge");
		location_shadowOffset = super.getUniformLocation("shadowOffset");
		location_outlineColor = super.getUniformLocation("outlineColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	public void loadColor(Vector4f color) {
		super.loadVector(location_color, color);
	}
	public void loadTranslation(Vector2f translation) {
		super.loadVector(location_translation, translation);
	}
	public void loadWidth(float width) {
		super.loadFloat(location_width, width);
	}
	public void loadEdge(float edge) {
		super.loadFloat(location_edge, edge);
	}
	public void loadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}
	public void loadBorderEdge(float borderEdge) {
		super.loadFloat(location_borderEdge, borderEdge);
	}
	public void loadShadowOffset(Vector2f offset) {
		super.loadVector(location_shadowOffset, offset);
	}
	public void loadOutlineColor(Vector4f color) {
		super.loadVector(location_outlineColor, color);
	}

}