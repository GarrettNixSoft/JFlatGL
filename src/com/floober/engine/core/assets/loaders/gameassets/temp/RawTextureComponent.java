package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.RawTexture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;

public record RawTextureComponent(String key, RawTexture texture, boolean hasTransparency) {

	public void addToGame() {
		TextureComponent textureComponent = texture.convertToOpenGLTexture();
		textureComponent.setHasTransparency(hasTransparency);
		Game.getTextures().addTexture(key, textureComponent);
	}

}
