package com.floober.engine.entity.effects.entity;

import com.floober.engine.entity.core.Entity;
import org.joml.Vector2f;

public class CameraShake extends EntityEffect {

	private final ShakeEffect xShake, yShake;

	public CameraShake(Entity parent) {
		super(parent);
		xShake = new ShakeEffect();
		yShake = new ShakeEffect();
		yShake.synchronize(xShake);
	}

	public void setSeverity(int xSeverity, int ySeverity) {
		xShake.setShakeSeverity(xSeverity);
		yShake.setShakeSeverity(ySeverity);
	}

	public void activate() {
		active = true;
		xShake.activate();
	}

	public void deactivate() {
		active = false;
		xShake.deactivate();
	}

	public Vector2f getOffset() {
//		if (!active) return new Vector2f(0, 0);
//		else
//		Logger.log(xShake.getOffset() + "," + yShake.getOffset());
			return new Vector2f(xShake.getOffset(), yShake.getOffset());
	}

	@Override
	public void update() {
		xShake.update();
		yShake.update();
	}

	@Override
	public void render() {
		// nothing
	}

	@Override
	public boolean remove() {
		return false;
	}
}
