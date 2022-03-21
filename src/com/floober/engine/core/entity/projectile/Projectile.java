package com.floober.engine.core.entity.projectile;

import com.floober.engine.core.entity.Entity;
import com.floober.engine.core.entity.util.EntityHandler;

public abstract class Projectile extends Entity {

	// attributes

	public Projectile(EntityHandler entityHandler, float x, float y) {
		super(x, y);
	}
}
