package com.gnix.jflatgl.core.assets.loaders.gameassets.temp;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;

public record RawFontType(String key, RawTextureAtlas rawAtlas, String fontFile, double aspectRatio) {

	public void addToGame() {
		FontType font = convert();
//		Logger.log("Added font (key: " + key + ") to the game. Atlas id: " + font.getTextureAtlas());
		Game.getFonts().addFont(key, font);
	}

	public FontType convert() {
		TextureAtlas fontAtlas = rawAtlas.convert();
		return new FontType(fontAtlas.texture().id(), fontFile, aspectRatio);
	}

}
