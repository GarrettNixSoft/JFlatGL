package com.gnix.jflatgl.core.gameState;

import com.gnix.jflatgl.core.gameState.transitions.StateTransitionManager;
import org.joml.Vector3f;

public abstract class GameStateManager {

	// game state array
	protected final GameState[] gameStates;
	protected int currentState;

	// changing states
	protected final StateTransitionManager transitionManager;

	public GameStateManager(int numStates) {
		gameStates = new GameState[numStates];
		transitionManager = new StateTransitionManager(this);
	}

	public abstract void init();

	public void transitionState(int state, float time, Vector3f color) {
		transitionManager.startTransition(state, time, color);
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		gameStates[currentState].init();
	}

	protected abstract void loadState(int state);

	private void unloadState(int state) {
		if (gameStates[state] != null) {
			gameStates[state].cleanUp();
			gameStates[state] = null;
		}
	}

	public void update() {
		if (transitionManager.transitionInProgress()) {
			transitionManager.update();
		}
		gameStates[currentState].handleInput();
		gameStates[currentState].update();
	}

	public void render() {
		gameStates[currentState].render();
		if (transitionManager.transitionInProgress()) {
			transitionManager.render();
		}
	}

	public GameState getCurrentState() {
		return gameStates[currentState];
	}

	public void cleanUp() {
		gameStates[currentState].cleanUp();
	}

}
