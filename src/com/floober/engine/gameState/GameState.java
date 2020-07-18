package com.floober.engine.gameState;

import com.floober.engine.main.Game;

public abstract class GameState {

	protected Game game;

	public GameState(Game game) {
		this.game = game;
	}

	public abstract void update();
	public abstract void render();
	public abstract void handleInput();

}