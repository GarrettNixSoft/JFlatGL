package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.animation.Animation;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.textures.TextureSet;

public record RawAnimation(String key, String textureSetKey, int frameTime) {

	public Animation convert() {
		TextureSet textureSet = Game.getTextureSet(textureSetKey);
		return new Animation(textureSet, frameTime);
	}

	public void addToGame() {
		Animation animation = convert();
		Game.getAnimations().addAnimation(key, animation);
	}

}
