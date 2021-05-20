package com.floober.engine.renderEngine.ppfx;

import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.renderEngine.models.QuadModel;
import com.floober.engine.renderEngine.ppfx.effects.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

/**
 * The PostProcessing class handles post-processing stages.
 * Post-processing effects should be implemented as classes
 * extending the PPEffect class.
 */
public class PostProcessing {

	// vertex data for the whole screen
	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static QuadModel quad;

	// effects to use
	private static final HashMap<String, PPEffect> effects = new HashMap<>();
	private static Contrast contrast;
	private static InvertColor invertColor;
	private static GaussianBlur gaussianBlur;
	private static Grayscale grayscale;

	// last stage, render to screen
	private static ToScreen toScreen;

	public static void init() {
		// generate the screen quad
		quad = ModelLoader.loadToVAO(POSITIONS, 2);
		// create the screen render stage
		toScreen = new ToScreen();
		// create the contrast change effect
		contrast = new Contrast();
		effects.put("contrast", contrast);
		// create the invert color effect
		invertColor = new InvertColor();
		effects.put("invertColor", invertColor);
		// create the gaussian blur effect
		gaussianBlur = new GaussianBlur();
		effects.put("gaussianBlur", gaussianBlur);
		// create the grayscale effect
		grayscale = new Grayscale();
		effects.put("grayscale", grayscale);
		// create other effects
		// ...

		// TEST
//		contrast.enable();
//		invertColor.enable();
		// END_TEST
	}

	/**
	 * Check if a Post Processing stage is enabled.
	 * @param stageID the ID of the stage
	 * @return true if enabled, false if disabled
	 */
	public static boolean isStageEnabled(String stageID) {
		return effects.get(stageID).isEnabled();
	}

	/**
	 * Turn a Post Processing stage on or off.
	 * @param stageID the ID of the stage
	 */
	public static void setStageEnabled(String stageID, boolean enabled) {
		if (enabled) effects.get(stageID).enable();
		else effects.get(stageID).disable();
	}

	public static void toggleStage(String stageID) {
		PPEffect effect = effects.get(stageID);
		if (effect.isEnabled()) effect.disable();
		else effect.enable();
	}

	public static void doPostProcessing(int colorTexture) {
		start();

		if (invertColor.isEnabled()) {
			invertColor.render(colorTexture);
			colorTexture = invertColor.getResult();
		}
		if (contrast.isEnabled()) {
			contrast.render(colorTexture);
			colorTexture = contrast.getResult();
		}
		if (gaussianBlur.isEnabled()) {
			gaussianBlur.render(colorTexture);
			colorTexture = gaussianBlur.getResult();
		}
		if (grayscale.isEnabled()) { // GRAYSCALE SHOULD BE LAST
			grayscale.render(colorTexture);
			colorTexture = grayscale.getResult();
		}
		// chain the rest ...

		// finally, ensure it's all rendered to the screen
		toScreen.render(colorTexture);

		end();
	}

	private static void start(){
		GL30.glBindVertexArray(quad.vaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public static void cleanUp() {
		contrast.cleanUp();
		invertColor.cleanUp();
	}

}