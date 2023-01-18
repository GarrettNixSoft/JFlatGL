package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.animation.Animation;
import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.util.time.Timer;

public abstract class EntityEffect extends Effect {

	protected Entity parent;
	protected Animation animation;

	public EntityEffect(Entity parent) {
		this.parent = parent;
		animation = new Animation();
		timer = new Timer(1);
	}

	protected boolean active;

	protected EntityEffect syncEffect;
	protected boolean synchronize;

	public void activate(float time) {
		timer.restart(time);
		active = true;
	}

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
			deactivate();
		else if (!active && syncEffect.active) {
			activate(syncEffect.timer.getTime());
		}
	}

	public boolean finished() {
		return timer.finished();
	}
}
