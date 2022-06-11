package com.floober.engine.core.assets.loaders.gameassets.temp;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.textures.TextureAtlas;
import com.floober.engine.core.util.Logger;

public record RawFontType(String key, RawTextureAtlas rawAtlas, String fontFile) {

	public void addToGame() {
		FontType font = convert();
		Logger.log("Added font (key: " + key + ") to the game. Atlas id: " + font.getTextureAtlas());
		Game.getFonts().addFont(key, font);
	}

	public FontType convert() {
		TextureAtlas fontAtlas = rawAtlas.convert();
		return new FontType(fontAtlas.textureID(), fontFile);
	}

}
