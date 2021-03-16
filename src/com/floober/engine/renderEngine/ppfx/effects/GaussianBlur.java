package com.floober.engine.renderEngine.ppfx.effects;

import com.floober.engine.renderEngine.ppfx.ImageRenderer;
import com.floober.engine.renderEngine.ppfx.PPEffect;
import com.floober.engine.renderEngine.shaders.blur.HorizontalBlurShader;
import com.floober.engine.renderEngine.shaders.blur.VerticalBlurShader;
import com.floober.engine.renderEngine.shaders.ppfx.ContrastShader;
import com.floober.engine.util.configuration.Config;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class GaussianBlur extends PPEffect {

	private float horizontalBlurAmount;
	private float verticalBlurAmount;

	private final HorizontalBlurShader horizontalBlurShader = new HorizontalBlurShader();
	private final VerticalBlurShader verticalBlurShader = new VerticalBlurShader();

	protected ImageRenderer stage1Renderer;

	private boolean secondStage = false;

	public GaussianBlur() {
		super(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		stage1Renderer = new ImageRenderer(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
		horizontalBlurAmount = 1.0f;
		verticalBlurAmount = 1.0f;
	}

	public void setHorizontalBlurAmount(float horizontalBlurAmount) {
		this.horizontalBlurAmount = horizontalBlurAmount;
	}

	public void setVerticalBlurAmount(float verticalBlurAmount) {
		this.verticalBlurAmount = verticalBlurAmount;
	}

	@Override
	public void init() {
		if (!secondStage) {
			horizontalBlurShader.loadTargetWidth(Config.INTERNAL_WIDTH * horizontalBlurAmount);
		}
		else {
			verticalBlurShader.loadTargetHeight(Config.INTERNAL_HEIGHT * verticalBlurAmount);
		}
	}

	@Override
	public void render(int texture) {
		horizontalBlurShader.start();
		init();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		stage1Renderer.renderQuad();
		horizontalBlurShader.stop();
		secondStage = true;
		verticalBlurShader.start();
		init();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, stage1Renderer.getOutputTexture());
		super.renderer.renderQuad();
		verticalBlurShader.stop();
		// done
	}
}