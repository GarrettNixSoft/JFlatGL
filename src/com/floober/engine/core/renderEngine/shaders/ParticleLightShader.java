package com.floober.engine.core.renderEngine.shaders;

public class ParticleLightShader extends ShaderProgram {

	private static final String VERTEX_FILE = SHADER_PATH + "particle/particleVertexLight.glsl";
	private static final String FRAGMENT_FILE = SHADER_PATH + "particle/particleFragmentLight.glsl";

	public ParticleLightShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {} // no uniforms; all data instanced!

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "transformationMatrix");
		super.bindAttribute(5, "color");
		super.bindAttribute(6, "innerRadius");
		super.bindAttribute(7, "outerRadius");
		super.bindAttribute(8, "lightMode");
		super.bindAttribute(9, "lightColor");
		super.bindAttribute(10, "lightIntensity");
	}
}