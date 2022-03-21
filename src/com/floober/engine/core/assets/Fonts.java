package com.floober.engine.core.assets;

import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;

import java.util.HashMap;
import java.util.Map;

/**
 * The Fonts class acts as a cache for storing Font data
 * loaded during the initial asset loading stage.
 */
public class Fonts {

	// STORING FONTS
	private final Map<String, FontType> fonts = new HashMap<>();

	// LOADING FONTS

	/**
	 * Store a font in the cache.
	 * @param key The ID to store this font with.
	 * @param font The font to store.
	 */
	public void addFont(String key, FontType font) {
		fonts.put(key, font);
	}

	// RETRIEVING FONTS

	/**
	 * Get a font from the cache.
	 * @param key The ID of the font to retrieve.
	 * @return The font with the given ID, or {@code null} if no font with the given ID exists.
	 */
	public FontType getFont(String key) { return fonts.get(key); }

}