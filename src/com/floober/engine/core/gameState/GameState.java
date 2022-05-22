package com.floober.engine.core.gameState;

import com.floober.engine.core.Game;

public abstract class GameState {

	protected GameStateManager gsm;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	// override when necessary
	public void init() {}

	public abstract void update();
	public abstract void render();
	public abstract void handleInput();

}