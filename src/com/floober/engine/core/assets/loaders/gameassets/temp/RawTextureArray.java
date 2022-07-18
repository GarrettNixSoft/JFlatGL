package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;
import com.floober.engine.core.renderEngine.textures.TextureComponent;

public record RawTextureArray(String key, RawTextureAtlas textureAtlas, int texWidth, int texHeight, boolean hasTransparency) {

	public void addToGame() {
		TextureAtlas convertedAtlas = textureAtlas.convert();
		int rows = convertedAtlas.height() / texHeight;
		int cols = convertedAtlas.width() / texWidth;
		TextureComponent[] textureArray = new TextureComponent[rows * cols];
		for (int i = 0; i < textureArray.length; i++) {
			textureArray[i] = convertedAtlas.texture().clone();
			textureArray[i].setTextureOffset(convertedAtlas.getTextureOffset(i));
			textureArray[i].setHasTransparency(hasTransparency);
		}
		Game.getTextures().addTextureArray(key, textureArray);
	}

}
