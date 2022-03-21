package com.floober.engine.core.renderEngine.shaders.textures;

import com.floober.engine.core.renderEngine.lights.Light;
import com.floober.engine.core.renderEngine.lights.LightMaster;
import com.floober.engine.core.renderEngine.shaders.ShaderCode;
import com.floober.engine.core.renderEngine.shaders.ShaderProgram;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.List;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class TextureShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureVertex.glsl";
	private static final String FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureFragment.glsl";
	private static final String COLOR_SWAP_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureColorSwap.glsl";
	private static final String LIGHT_FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureLight.glsl";
	private static final String GLITCH_FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureGlitch.glsl";
	private static final String FADE_FRAGMENT_FILE = "/com/floober/engine/core/renderEngine/shaders/shadercode/texture/textureFade.glsl";

	// standard texture shader
	private int location_transformationMatrix;
	private int location_textureOffset;
	private int location_doLighting;
	private int location_textureAlpha;

	private int location_textureSampler;
	private int location_noiseSampler;

	// color swap channels
	private int location_doColorSwap;
	private int location_rChannelColor;
	private int location_gChannelColor;
	private int location_bChannelColor;
	private int location_aChannelColor;

	// lights!
	private int location_screenRatio;
	private int location_ambientLight;
	private int[] location_lightPositions;
	private int[] location_lightColors;
	private int[] location_lightIntensities;
	private int[] location_lightInnerRadii;
	private int[] location_lightOuterRadii;
	private int[] location_lightMaxRadii;

	// color effect
	private int location_doColor;
	private int location_color;
	private int location_mix;

	// glitch shader
	private int location_doGlitch;
	private int location_time;
	private int location_amplitude;
	private int location_speed;

	// fade shader
	private int location_doFade;
	private int location_fadeValues;
	private int location_fadeOffsets;
	private int location_fadeDirection;

	public TextureShader() {
		super(
				new ShaderCode(VERTEX_FILE, GL_VERTEX_SHADER),
				new ShaderCode(FRAGMENT_FILE, GL_FRAGMENT_SHADER),
				new ShaderCode(COLOR_SWAP_FILE, GL_FRAGMENT_SHADER),
				new ShaderCode(LIGHT_FRAGMENT_FILE, GL_FRAGMENT_SHADER),
				new ShaderCode(GLITCH_FRAGMENT_FILE, GL_FRAGMENT_SHADER),
				new ShaderCode(FADE_FRAGMENT_FILE, GL_FRAGMENT_SHADER)
		);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_textureOffset = super.getUniformLocation("textureOffset");
		location_doLighting = super.getUniformLocation("doLighting");
		location_textureAlpha = super.getUniformLocation("textureAlpha");
		// texture units
		location_textureSampler = super.getUniformLocation("textureSampler");
		location_noiseSampler = super.getUniformLocation("noiseSampler");
		// color swap uniforms
		location_doColorSwap = super.getUniformLocation("doColorSwap");
		location_rChannelColor = super.getUniformLocation("rChannelColor");
		location_gChannelColor = super.getUniformLocation("gChannelColor");
		location_bChannelColor = super.getUniformLocation("bChannelColor");
		location_aChannelColor = super.getUniformLocation("aChannelColor");
		// lighting uniforms
		location_screenRatio = super.getUniformLocation("screenRatio");
		location_ambientLight = super.getUniformLocation("ambientLight");
		// light properties uniforms
		location_lightPositions = new int[LightMaster.MAX_LIGHTS];
		location_lightColors = new int[LightMaster.MAX_LIGHTS];
		location_lightIntensities = new int[LightMaster.MAX_LIGHTS];
		location_lightInnerRadii = new int[LightMaster.MAX_LIGHTS];
		location_lightOuterRadii = new int[LightMaster.MAX_LIGHTS];
		location_lightMaxRadii = new int[LightMaster.MAX_LIGHTS];
		for (int i = 0; i < LightMaster.MAX_LIGHTS; ++i) {
			location_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			location_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			location_lightIntensities[i] = super.getUniformLocation("lightIntensities[" + i + "]");
			location_lightInnerRadii[i] = super.getUniformLocation("lightInnerRadii[" + i + "]");
			location_lightOuterRadii[i] = super.getUniformLocation("lightOuterRadii[" + i + "]");
			location_lightMaxRadii[i] = super.getUniformLocation("lightMaxRadii[" + i + "]");
		}
		// color uniforms
		location_doColor = super.getUniformLocation("doColor");
		location_color = super.getUniformLocation("color");
		location_mix = super.getUniformLocation("mixVal");
		// glitch uniforms
		location_doGlitch = super.getUniformLocation("doGlitch");
		location_time = super.getUniformLocation("time");
		location_amplitude = super.getUniformLocation("AMPLITUDE");
		location_speed = super.getUniformLocation("SPEED");
		// fade uniforms
		location_doFade = super.getUniformLocation("doFade");
		location_fadeValues = super.getUniformLocation("minmax");
		location_fadeOffsets = super.getUniformLocation("offsets");
		location_fadeDirection = super.getUniformLocation("direction");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void connectTextureUnits() {
		super.loadInt(location_textureSampler, 0);
		super.loadInt(location_noiseSampler, 1);
	}

	// uniforms
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	public void loadTextureOffset(Vector4f textureOffset) {
		super.loadVector(location_textureOffset, textureOffset);
	}
	public void loadTextureAlpha(float textureAlpha) { super.loadFloat(location_textureAlpha, textureAlpha); }

	// turn effects on or off
	public void loadDoColorSwap(boolean doColorSwap) { super.loadBoolean(location_doColorSwap, doColorSwap); }
	public void loadDoLighting(boolean doLighting) { super.loadBoolean(location_doLighting, doLighting); }
	public void loadDoColor(boolean doColor) { super.loadBoolean(location_doColor, doColor); }
	public void loadDoGlitch(boolean doGlitch) {
		super.loadBoolean(location_doGlitch, doGlitch);
	}
	public void loadDoFade(boolean doFade) { super.loadBoolean(location_doFade, doFade); }

	// color swap
	public void loadrChannelColor(Vector4f rChannelColor) { super.loadVector(location_rChannelColor, rChannelColor); }
	public void loadgChannelColor(Vector4f gChannelColor) { super.loadVector(location_gChannelColor, gChannelColor); }
	public void loadbChannelColor(Vector4f bChannelColor) { super.loadVector(location_bChannelColor, bChannelColor); }
	public void loadaChannelColor(Vector4f aChannelColor) { super.loadVector(location_aChannelColor, aChannelColor); }

	// light
	public void loadScreenRatio(Vector2f ratio) { super.loadVector(location_screenRatio, ratio); }
	public void loadAmbientLight(float ambientLight) { super.loadFloat(location_ambientLight, ambientLight); }

	public void loadLights(List<Light> lights) {
		int size = lights.size();
		for (int i = 0; i < LightMaster.MAX_LIGHTS; ++i) {
			if (i < size) {
				super.loadVector(location_lightPositions[i], lights.get(i).position());
				super.loadVector(location_lightColors[i], lights.get(i).color());
				super.loadFloat(location_lightIntensities[i], lights.get(i).intensity());
				super.loadFloat(location_lightInnerRadii[i], lights.get(i).innerRadius());
				super.loadFloat(location_lightOuterRadii[i], lights.get(i).outerRadius());
				super.loadFloat(location_lightMaxRadii[i], lights.get(i).maxRadius());
			}
			else {
				super.loadVector(location_lightPositions[i], new Vector2f(0));
				super.loadVector(location_lightColors[i], new Vector4f(0));
				super.loadFloat(location_lightIntensities[i], 0);
				super.loadFloat(location_lightInnerRadii[i], 0);
				super.loadFloat(location_lightOuterRadii[i], -1); // loading -1 tells the shader to skip this light, since it doesn't exist
				super.loadFloat(location_lightMaxRadii[i], -1);
			}
		}
	}

	// color
	public void loadColor(Vector4f color) { super.loadVector(location_color, color); }
	public void loadMix(float mix) { super.loadFloat(location_mix, mix); }

	// glitch
	public void loadTime(float time) {
		super.loadFloat(location_time, time);
	}
	public void loadAmplitude(float amplitude) { super.loadFloat(location_amplitude, amplitude); }
	public void loadSpeed(float speed) { super.loadFloat(location_speed, speed); }

	// fade
	public void loadFadeValues(Vector2f values) { super.loadVector(location_fadeValues, values); }
	public void loadFadeOffsets(Vector2f offsets) { super.loadVector(location_fadeOffsets, offsets); }
	public void loadFadeDirection(int direction) { super.loadInt(location_fadeDirection, direction); }

}