package com.floober.engine.test.gameState;

import com.floober.engine.core.gameState.GameStateManager;

public class TestGameStateManager extends GameStateManager {

	public TestGameStateManager(int numStates) {
		super(numStates);
	}

	@Override
	public void init() {
		currentState = 0;
		setState(currentState);
	}

	@Override
	protected void loadState(int state) {
		gameStates[currentState] = new TestState(this);
	}
}
