package com.floober.engine.core.renderEngine.ppfx;

import com.floober.engine.core.renderEngine.models.ModelLoader;
import com.floober.engine.core.renderEngine.models.QuadModel;
import com.floober.engine.core.renderEngine.ppfx.effects.Contrast;
import com.floober.engine.core.renderEngine.ppfx.effects.GaussianBlur;
import com.floober.engine.core.renderEngine.ppfx.effects.InvertColor;
import com.floober.engine.core.renderEngine.ppfx.effects.ToScreen;
import com.floober.engine.core.util.Logger;
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
	private final QuadModel quad;

	// effects to use
	private final HashMap<String, PPEffect> effects = new HashMap<>();
	private final Contrast contrast;
	private final InvertColor invertColor;
	private final GaussianBlur gaussianBlur;

	// last stage, render to screen
	private final ToScreen toScreen;

	public PostProcessing(long windowID) {
		// generate the screen quad
		quad = ModelLoader.loadToVAO(POSITIONS, 2);
		// create the screen render stage
		toScreen = new ToScreen(windowID);
		// create the contrast change effect
		contrast = new Contrast(windowID);
		effects.put("contrast", contrast);
		// create the invert color effect
		invertColor = new InvertColor(windowID);
		effects.put("invertColor", invertColor);
		// create the gaussian blur effect
		gaussianBlur = new GaussianBlur(windowID);
		effects.put("gaussianBlur", gaussianBlur);
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
	public boolean isStageEnabled(String stageID) {
		return effects.get(stageID).isEnabled();
	}

	/**
	 * Turn a Post Processing stage on or off.
	 * @param stageID the ID of the stage
	 */
	public void setStageEnabled(String stageID, boolean enabled) {
		if (enabled) {
			if (isStageEnabled(stageID)) Logger.logWarning("Tried to enable post-processing stage \"" + stageID + "\", but that stage was already enabled.");
			effects.get(stageID).enable();
		}
		else {
			if (!isStageEnabled(stageID)) Logger.logWarning("Tried to disable post-processing stage \"" + stageID + "\", but that stage was already disabled.");
			effects.get(stageID).disable();
		}
	}

	public void toggleStageEnabled(String stageID) {
		setStageEnabled(stageID, !isStageEnabled(stageID));
	}

	public void doPostProcessing(int colorTexture) {
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
		// chain the rest ...

		// finally, ensure it's all rendered to the screen
		toScreen.render(colorTexture);

		end();
	}

	private void start(){
		GL30.glBindVertexArray(quad.vaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	public void cleanUp() {
		contrast.cleanUp();
		invertColor.cleanUp();
	}

}