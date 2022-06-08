package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.renderEngine.textures.TextureSet;

public record RawTextureSet(String key, RawTexture texture, int texWidth, int texHeight, boolean hasTransparency) {

	public void addToGame() {
		TextureComponent textureComponent = texture.convertToOpenGLTexture();
		TextureSet textureSet = new TextureSet(textureComponent, texWidth, texHeight, hasTransparency);
		Game.getTextures().addTextureSet(key, textureSet);
	}

}
