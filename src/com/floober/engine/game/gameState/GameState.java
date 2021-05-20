package com.floober.engine.game.gameState;

import com.floober.engine.game.Game;

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