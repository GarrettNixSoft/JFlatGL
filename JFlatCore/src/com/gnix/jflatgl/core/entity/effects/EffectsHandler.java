package com.gnix.jflatgl.core.entity.effects;

import com.gnix.jflatgl.core.camera.Camera;

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
	
	// effects
	private final List<Effect> effects;
	
	// create handler
	public EffectsHandler() {
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
		Camera camera = Camera.getInstance();
		for (Effect e : effects) e.render(camera);
	}
	
}
