package com.gnix.jflatgl.core.gameState;

import java.lang.reflect.Constructor;

public class CustomStateManager extends GameStateManager {

	private final Class<? extends GameState> stateType;

	public CustomStateManager(Class<? extends GameState> type) {
		super(1);
		stateType = type;
	}

	@Override
	public void init() {
		setState(0);
	}

	@Override
	protected void loadState(int state) {
		try {
			Constructor<? extends GameState> constructor = stateType.getConstructor(GameStateManager.class);
			gameStates[currentState] = constructor.newInstance(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
