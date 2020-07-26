package com.floober.engine.lights;

import java.util.ArrayList;
import java.util.List;

public class LightMaster {

	private static final int MAX_LIGHTS = 8;

	private static final List<Light> sceneLights = new ArrayList<>(MAX_LIGHTS);
	private static int numLights = 0;

	private static float ambientLight = 1;

	public static float getAmbientLight() {
		return ambientLight;
	}

	public static void setAmbientLight(float ambient) {
		ambientLight = ambient;
	}

	public static List<Light> getSceneLights() {
		return sceneLights;
	}

	public static void addLight(Light light) {
		if (numLights < MAX_LIGHTS) {
			sceneLights.add(light);
			numLights++;
		}
	}

	public void removeLight(Light light) {
		sceneLights.remove(light);
		numLights--;
	}

	public void clearLights() {
		sceneLights.clear();
		numLights = 0;
	}

}