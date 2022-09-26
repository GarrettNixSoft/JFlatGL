package com.gnix.jflatgl.core.assets.loaders.gameassets.temp;

import com.gnix.jflatgl.animation.Animation;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.textures.TextureSet;

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
