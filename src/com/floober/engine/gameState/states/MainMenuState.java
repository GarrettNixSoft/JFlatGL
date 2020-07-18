package com.floober.engine.gameState.states;

import com.floober.engine.gameState.GameState;
import com.floober.engine.main.Game;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.util.Colors;

public class MainMenuState extends GameState {

	public MainMenuState(Game game) {
		super(game);
	}

	@Override
	public void update() {
		// do stuff
	}

	@Override
	public void render() {
		Render.drawRect(Colors.RED, 100, 100, 0, 64, 64, false);
		Render.drawRect(Colors.GREEN, 200, 200, 1, 300, 20, false);
		Render.drawRect(Colors.YELLOW, 100, 100, 2, 128, 128, false);

		Render.drawCircle(Colors.GREEN, 500, 500, 0, 35, 40, 0.75f, 0);

		Render.drawLine(Colors.CYAN, 10, 10, 500, 10, 0, 1);

		Render.drawOutline(Colors.ORANGE, 500, 100, 0, 30, 30, true);
		Render.drawOutline(Colors.ORANGE, 500, 150, 0, 30, 30, 4, true);
	}

	@Override
	public void handleInput() {
		// do stuff
	}
}