package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.util.Logger;
import org.joml.Vector2f;

public class Shake2D extends Effect {

	private final ShakeEffect xShake, yShake;
	private boolean active;

	public Shake2D() {
		xShake = new ShakeEffect();
		yShake = new ShakeEffect();
		yShake.synchronize(xShake);
	}

	public void setSeverityInitial(int xSeverity, int ySeverity) {
		xShake.setShakeSeverityInitial(xSeverity);
		yShake.setShakeSeverityInitial(ySeverity);
	}

	public void setSeverityFinal(int xSeverity, int ySeverity) {
		xShake.setShakeSeverityFinal(xSeverity);
		yShake.setShakeSeverityFinal(ySeverity);
	}

	public void activate(float time) {
		active = true;
		xShake.activate(time);
	}

	public void deactivate() {
		active = false;
		xShake.deactivate();
	}

	public boolean isActive() {
		return xShake.isActive();
	}

	public Vector2f getOffset() {
		if (!active) return new Vector2f(0, 0);
//		Logger.log(xShake.getOffset() + "," + yShake.getOffset());
		return new Vector2f(xShake.getOffset(), yShake.getOffset());
	}

	@Override
	public void update() {
		xShake.update();
		yShake.update();
	}

	@Override
	public void render(Camera camera) {
		// nothing
	}

	@Override
	public boolean remove() {
		return false;
	}
}
