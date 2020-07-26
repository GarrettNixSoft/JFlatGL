package com.floober.engine.entity;

public abstract class Entity {

	// position in world
	protected float x, y;

	// size in pixels
	protected float width, height;

	// motion
	protected float dx, dy;

	public abstract void update();
	public abstract void render();

}