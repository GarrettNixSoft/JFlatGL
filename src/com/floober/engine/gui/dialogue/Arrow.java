package com.floober.engine.gui.dialogue;

import com.floober.engine.game.Game;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.textures.TextureComponent;
import com.floober.engine.util.interpolators.PulseFloat;

public class Arrow {

	private final TextureElement texture;
	private final float x, y;

	private final PulseFloat interpolator;
	private final float range;

	public Arrow(float x, float y, int layer) {
		TextureComponent tex = Game.getTexture("dialogue_arrow");
		texture = new TextureElement(tex, x, y, layer, 16, 16, true);
		texture.setHasTransparency(true);
		this.x = x;
		this.y = y;
		interpolator = new PulseFloat(150);
		range = 25;
		interpolator.start();
	}

	public void reset() {
		interpolator.reset();
	}

	public void update() {
		interpolator.update();
	}

	public void render() {
		texture.setPosition(x + interpolator.getValue() * range, y, texture.getLayer());
		texture.transform();
		Render.drawImage(texture);
	}

}