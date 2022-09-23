package com.gnix.jflatgl.core.entity.pickup;

import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.entity.player.Player;
import com.gnix.jflatgl.core.entity.util.EntityHandler;

public abstract class Pickup extends Entity {

	protected EntityHandler entityHandler;

	public Pickup(float x, float y, EntityHandler entityHandler) {
		super(x, y);
		this.entityHandler = entityHandler;
		textureElement.setHasTransparency(true);
	}

	// action on player pickup
	public abstract void pickUp(Player player);

	@Override
	public abstract void update();

	@Override
	public abstract void render();

}
