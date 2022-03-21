package com.floober.engine.core.entity.pickup;

import com.floober.engine.core.entity.Entity;
import com.floober.engine.core.entity.player.Player;
import com.floober.engine.core.entity.util.EntityHandler;

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