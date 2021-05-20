package com.floober.engine.entity.core.player;

public class NullPlayer extends Player {
	/**
	 * Create a non-functioning dummy Player.
	 */
	public NullPlayer() {
		super(null, 0, 0);
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
