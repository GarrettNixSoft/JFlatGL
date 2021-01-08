package com.floober.engine.entity.effects;

import com.floober.engine.game.Game;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Floober
 * 
 * The EffectsManager handles visual effects in the game.
 * Things like explosions or flashes are handled here.
 * 
 */
public class EffectsHandler {
	
	// game
	private final Game game;
	
	// effects
	private final List<Effect> effects;
	
	// create handler
	public EffectsHandler(Game game) {
		this.game = game;
		effects = new ArrayList<>();
	}
	
	// add effect
	public void addEffect(Effect effect) {
		effects.add(effect);
	}
	
	// update
	public void update() {
		for (int i = 0; i < effects.size(); i++) {
			effects.get(i).update();
			if (effects.get(i).remove()) {
				effects.remove(i);
				i--;
			}
		}
	}
	
	// render queue
	public void render() {
		for (Effect e : effects) e.render();
	}
	
}