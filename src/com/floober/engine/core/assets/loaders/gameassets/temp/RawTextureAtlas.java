package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;
import com.floober.engine.core.renderEngine.textures.TextureComponent;

public record RawTextureAtlas(String key, RawTexture texture, int numRows, boolean hasTransparency) {

	public void addToGame() {
		TextureAtlas textureAtlas = convert();
		Game.getTextures().addTextureAtlas(key, textureAtlas);
	}

	public TextureAtlas convert() {
		TextureComponent temp = texture.convertToOpenGLTexture();
		return new TextureAtlas(temp, numRows, hasTransparency);
	}

}
