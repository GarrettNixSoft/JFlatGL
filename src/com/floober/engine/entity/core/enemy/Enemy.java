package com.floober.engine.entity.core.enemy;

import com.floober.engine.entity.core.Entity;
import com.floober.engine.entity.util.EntityHandler;
import com.floober.engine.game.Game;
import com.floober.engine.util.time.TimeScale;

public abstract class Enemy extends Entity {

	// damage to player
	protected float damage;

	// invincibility
	protected long invincibilityTimer;
	protected int invincibilityDuration;
	protected boolean invincible;

	// (optional) can jump
	protected boolean jumping;
	protected float stopJumpSpeed;

	public Enemy(EntityHandler entityHandler, float x, float y) {
		super(entityHandler, x, y);
		this.x = x;
		this.y = y;
		textureElement.setHasTransparency(true);
	}

	// INTERACTIONS
	public abstract void damage(float damage);

	// GETTERS
	public float getDamage() { return damage; }
	public boolean isInvincible() { return invincible; }

	protected void checkInvincibility() {
		if (invincible) {
			long time = TimeScale.getScaledTime(invincibilityTimer);
			if (time > invincibilityDuration) {
				invincible = false;
			}
		}
	}

}