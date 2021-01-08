package com.floober.engine.entity.effects.entity;

import com.floober.engine.entity.core.Entity;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.util.interpolators.FadeFloat;
import org.joml.Vector4f;

public class FlashEffect extends EntityEffect {

	private final FadeFloat fade;
	private final Vector4f color;

	public FlashEffect(Entity parent, Vector4f color) {
		super(parent);
		this.color = new Vector4f(color);
		fade = new FadeFloat();
	}

	public void start(float time) {
		// reset the timer, set the new target time
		fade.reset();
		fade.setTime(time);
		// enable the effect on the target element
		parent.getTextureElement().setDoColor(true);
		// start the timer
		fade.start();
	}

	public boolean done() {
		return fade.finished() || !fade.isRunning();
	}

	@Override
	public void update() {
		fade.update();
	}

	@Override
	public void render() {
		if (fade.started()) {
			TextureElement targetElement = parent.getTextureElement();
			float value = fade.getValue();
			if (value == 0) {
				// done; disable the effect, reset the fade value
				targetElement.setDoColor(false);
				fade.reset();
			}
			else {
				// update the effect color
				targetElement.setColor(color);
				targetElement.setMix(value);
			}
		}
	}

	@Override
	public boolean remove() {
		return false;
	}
}
