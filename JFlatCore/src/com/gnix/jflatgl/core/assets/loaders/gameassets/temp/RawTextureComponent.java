package com.gnix.jflatgl.core.assets.loaders.gameassets.temp;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.assets.loaders.RawTexture;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;

public record RawTextureComponent(String key, RawTexture texture, boolean hasTransparency) {

	public void addToGame() {
		TextureComponent textureComponent = texture.convertToOpenGLTexture();
		textureComponent.setHasTransparency(hasTransparency);
		Game.getTextures().addTexture(key, textureComponent);
	}

}
