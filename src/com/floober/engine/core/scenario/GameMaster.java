package com.floober.engine.core.scenario;

import com.floober.engine.core.Game;
import com.floober.gametitle.gameState.GameState;
import com.floober.gametitle.gameState.GameStateManager;
import com.floober.gametitle.gameState.states.MainMenuState;
import com.floober.gametitle.gameState.states.PlayState;

/**
 * The GameMaster controls what content is played when the player
 * advances to the next level, and keeps records of player choices.
 */
public class GameMaster {

	public static GameState getStartingState(Game game, GameStateManager gsm) {
		// check for on-start flags
		// ...
		// DEFAULT: Load to the menu
		return new MainMenuState(game, gsm);
	}

	public static PlayState getNextLevelState(Game game, GameStateManager gsm) {
		// TODO implement some more flag logic
		return new PlayState(game, gsm);
	}

}