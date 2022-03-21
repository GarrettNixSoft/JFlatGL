package com.floober.engine.core.entity.player;

import com.floober.engine.core.entity.Entity;
import com.floober.engine.core.entity.util.EntityHandler;

public class Player extends Entity {

	/**
	 * Create the Player.
	 * @param entityHandler The EntityHandler.
	 * @param x The starting x-position of the player.
	 * @param y The starting y-position of the player.
	 */
	public Player(EntityHandler entityHandler, float x, float y) {
		super(x, y);
		// implement
	}

	// GETTERS
	public float getHealth() {
		return health;
	}
	public float getMaxHealth() {
		return maxHealth;
	}

	// SETTERS

	// updating
	protected void checkInput() {
		// implement
	}

	@Override
	public void damage(float damage) {
		// implement
	}

	@Override
	public void update() {
		// implement
	}

	public void updateNoMotion() {
		// implement
	}

	@Override
	public void render() {
		// implement
	}

	@Override
	protected void updateTextureElement() {
		// implement
	}

}