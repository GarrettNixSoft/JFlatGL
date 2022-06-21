package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;

public record RawTextureSet(String key, RawTexture texture, int texWidth, int texHeight, boolean hasTransparency) {

	public TextureSet convert() {
		TextureComponent textureComponent = texture.convertToOpenGLTexture();
		return new TextureSet(textureComponent, texWidth, texHeight, hasTransparency);
	}

	public void addToGame() {
		TextureSet textureSet = convert();
		Game.getTextures().addTextureSet(key, textureSet);
	}

}
