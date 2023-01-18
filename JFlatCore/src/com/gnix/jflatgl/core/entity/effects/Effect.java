package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.core.camera.Camera;
import com.gnix.jflatgl.core.util.time.Timer;

public abstract class Effect {

	protected Timer timer;

	public abstract void update();
	public abstract void render(Camera camera);
	
	// getters
	public abstract boolean remove();
	
}
