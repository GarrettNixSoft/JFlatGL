package com.gnix.jflatgl.core.entity.projectile;

import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.entity.util.EntityHandler;

public abstract class Projectile extends Entity {

	// attributes

	public Projectile(EntityHandler entityHandler, float x, float y) {
		super(x, y);
	}
}
