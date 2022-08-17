package com.floober.engine.core.gameState;

import com.floober.engine.core.Game;

public abstract class GameState {

	protected GameStateManager gsm;
	protected String stateID;

	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	/**
	 * This method is called once immediately after this {@code GameState} is constructed. Any setup code you don't want
	 * to include in the constructor can be included here to reduce clutter.
	 */
	public abstract void init();

	/**
	 * This method is called once per frame, prior to {@code update()}.
	 */
	public abstract void handleInput();

	/**
	 * This method is called once per frame. Game logic should be handled here. For any game logic that requires time
	 * information, you can get the amount of time (in seconds) that passed between the previous frame and this one by
	 * calling {@code Game.getFrameTime()}.
	 */
	public abstract void update();

	/**
	 *
	 */
	public abstract void render();

	/**
	 * This method is called once when this {@code GameState} is unloaded. Any resources that need to be cleaned up or
	 * removed (such as {@code GUIText}s) should be handled here.
	 */
	public abstract void cleanUp();

	public String getStateID() {
		return stateID;
	}

}