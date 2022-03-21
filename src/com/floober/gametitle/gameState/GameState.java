package com.floober.gametitle.gameState;

import com.floober.engine.core.Game;

public abstract class GameState {

	protected Game game;
	protected GameStateManager gsm;

	public GameState(Game game, GameStateManager gsm) {
		this.game = game;
		this.gsm = gsm;
	}

	// override when necessary
	public void init() {}

	public abstract void update();
	public abstract void render();
	public abstract void handleInput();

}