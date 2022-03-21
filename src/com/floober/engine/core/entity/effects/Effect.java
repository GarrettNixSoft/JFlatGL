package com.floober.engine.core.entity.effects;

import com.floober.engine.core.entity.Entity;

public abstract class Effect extends Entity {

	public Effect() {
		super(0, 0);
	}

	// actions
	public abstract void update();
	public abstract void render();
	
	// getters
	public abstract boolean remove();
	
}