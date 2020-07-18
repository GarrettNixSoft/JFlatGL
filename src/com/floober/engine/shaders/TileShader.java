package com.floober.engine.shaders;

public class TileShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/shaders/shadercode/tileVertex";
	private static final String FRAGMENT_FILE = "/com/floober/engine/shaders/shadercode/tileFragment";

	private int location_numRows;

	public TileShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numRows = super.getUniformLocation("numRows");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "transformationMatrix");
		super.bindAttribute(5, "texOffsets");
	}
	// load uniforms
	public void loadNumRows(float numRows) { super.loadFloat(location_numRows, numRows); }

}
