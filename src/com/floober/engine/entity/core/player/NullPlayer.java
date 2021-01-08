package com.floober.engine.entity.core.player;

import com.floober.engine.entity.util.EntityHandler;

public class NullPlayer extends Player {
	/**
	 * Create a non-functioning dummy Player.
	 */
	public NullPlayer(EntityHandler entityHandler) {
		super(entityHandler, 0, 0);
	}

	@Override
	public void update() {
		// do nothing
	}

	@Override
	public void updateNoMotion() {
		// do nothing
	}

	@Override
	public void render() {
		// don't render anything
	}
}
