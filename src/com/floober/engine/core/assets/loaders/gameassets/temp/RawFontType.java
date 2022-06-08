package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;

public record RawFontType(String key, RawTextureAtlas rawAtlas, String fontFile) {

	public void addToGame() {
		FontType font = convert();
		Game.getFonts().addFont(key, font);
	}

	public FontType convert() {
		TextureAtlas fontAtlas = rawAtlas.convert();
		return new FontType(fontAtlas.textureID(), fontFile);
	}

}
