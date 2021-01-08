package com.floober.engine.entity.projectile;

import com.floober.engine.entity.core.Entity;
import com.floober.engine.entity.util.EntityHandler;

public abstract class Projectile extends Entity {

	// attributes

	public Projectile(EntityHandler entityHandler, float x, float y) {
		super(entityHandler, x, y);
	}
}
