package com.floober.engine.test.gameState;

import com.floober.engine.core.gameState.GameStateManager;

public class TestGameStateManager extends GameStateManager {

	public TestGameStateManager(int numStates) {
		super(numStates);
	}

	@Override
	public void init() {
		// sample
	}

	@Override
	protected void loadState(int state) {
		gameStates[currentState] = new TestState(this);
	}
}
