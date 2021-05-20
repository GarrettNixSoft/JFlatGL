package com.floober.engine.game.gameState;

import com.floober.engine.game.Game;
import com.floober.engine.game.gameState.states.MainMenuState;
import com.floober.engine.game.gameState.states.PlayState;
import com.floober.engine.game.gameState.states.SettingsState;

public class GameStateManager {

	// state IDs
	public static final int MAIN_MENU_STATE = 0;
	public static final int SETTINGS_STATE = 1;
	public static final int PLAY_STATE = 2;

	// game state array
	private static final int NUM_STATES = 3;
	private final GameState[] gameStates;
	private int currentState;

	// game access for initializing game states
	private final Game game;

	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new GameState[NUM_STATES];
		currentState = MAIN_MENU_STATE;
		setState(currentState);
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		gameStates[currentState].init();
	}

	private void loadState(int state) {
		switch (state) {
			case MAIN_MENU_STATE -> gameStates[state] = new MainMenuState(game, this);
			case SETTINGS_STATE -> gameStates[state] = new SettingsState(game, this);
			case PLAY_STATE -> gameStates[state] = new PlayState(game, this);
		}
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void update() {
		gameStates[currentState].handleInput();
		gameStates[currentState].update();
	}

	public void render() {
		gameStates[currentState].render();
	}

	public GameState getCurrentState() {
		return gameStates[currentState];
	}

}