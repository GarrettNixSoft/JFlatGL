package com.floober.engine.entity.effects.entity;

import com.floober.engine.animation.Animation;
import com.floober.engine.entity.core.Entity;
import com.floober.engine.entity.effects.Effect;

public abstract class EntityEffect extends Effect {

	protected Entity parent;
	protected Animation animation;

	public EntityEffect(Entity parent) {
		this.parent = parent;
		animation = new Animation();
	}

	protected long timer;
	protected boolean active;

	protected EntityEffect syncEffect;
	protected boolean synchronize;

	public void activate() { active = true; }
	public void deactivate() { active = false; }
	public boolean isActive() { return active; }

	public void synchronize(EntityEffect e) {
		syncEffect = e;
		synchronize = true;
	}

	public void desync() {
		syncEffect = null;
		synchronize = false;
	}

	protected void checkSync() {
		if (active && !syncEffect.active)
			active = false;
		else if (!active && syncEffect.active) {
			active = true;
			timer = System.nanoTime();
		}
	}

	public abstract void update();
	public abstract void render();
}
