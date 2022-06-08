package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;

public record RawTextureArray(String key, RawTexture[] textures, boolean[] hasTransparency) {

	public void addToGame() {
		TextureComponent[] textureArray = new TextureComponent[textures.length];
		for (int i = 0; i < textures.length; i++) {
			textureArray[i] = textures[i].convertToOpenGLTexture();
			textureArray[i].setHasTransparency(hasTransparency[i]);
		}
		Game.getTextures().addTextureArray(key, textureArray);
	}

}
