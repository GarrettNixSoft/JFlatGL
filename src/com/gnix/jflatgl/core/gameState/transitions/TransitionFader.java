package com.gnix.jflatgl.core.gameState.transitions;

import com.gnix.jflatgl.core.renderEngine.Render;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import org.joml.Vector4f;

public class TransitionFader {

	private static final Vector4f fadeColor = new Vector4f();
	private static boolean doFade;

	public static void setDoFade(boolean fade) {
		doFade = fade;
	}

	public static void setFadeColor(Vector4f color) {
		fadeColor.set(color);
	}

	public static void render() {
		if (doFade) Render.fillScreen(fadeColor, Layers.TOP_LAYER);
	}

}
