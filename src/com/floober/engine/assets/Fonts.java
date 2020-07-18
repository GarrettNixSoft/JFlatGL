package com.floober.engine.assets;

import com.floober.engine.fonts.fontMeshCreator.FontType;

import java.util.HashMap;
import java.util.Map;

public class Fonts {

	// STORING FONTS
	private final Map<String, FontType> fonts = new HashMap<>();

	// LOADING FONTS
	public void addFont(String key, FontType font) {
		fonts.put(key, font);
	}

	// RETRIEVING FONTS
	public FontType getFont(String key) { return fonts.get(key); }


}