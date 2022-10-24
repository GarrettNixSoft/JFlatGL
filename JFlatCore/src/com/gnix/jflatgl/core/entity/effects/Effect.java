package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.core.entity.Entity;

public abstract class Effect extends Entity {

	public Effect() {
		super(0, 0);
	}
	
	// getters
	public abstract boolean remove();
	
}
