package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.animation.Animation;
import com.floober.engine.animation.GlitchAnimation;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.textures.TextureSet;

public record RawGlitchAnimation(String key, RawAnimation rawBase, String glitchTextureSetKey, int glitchDelay) {

	public GlitchAnimation convert() {
		Animation base = rawBase.convert();
		TextureSet glitchSet = Game.getTextureSet(glitchTextureSetKey);
		GlitchAnimation result = new GlitchAnimation(base, glitchSet);
		result.setGlitchDelay(glitchDelay);
		return result;
	}

	public void addToGame() {
		GlitchAnimation glitchAnimation = convert();
		Game.getAnimations().addGlitchAnimation(key, glitchAnimation);
	}

}
