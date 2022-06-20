package com.floober.engine.core.gameState;

public abstract class GameState {

	protected GameStateManager gsm;
	protected String stateID;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	public abstract void init();
	public abstract void update();
	public abstract void render();
	public abstract void handleInput();
	public abstract void cleanUp();

	public String getStateID() {
		return stateID;
	}

}