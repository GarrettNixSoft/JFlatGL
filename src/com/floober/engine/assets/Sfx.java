package com.floober.engine.assets;

import com.floober.engine.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class Sfx {

	private final Map<String, Sound> sfx = new HashMap<>();

	public void addSfx(String key, Sound sound) {
		sfx.put(key, sound);
	}

	public Sound getSfx(String key) {
		return sfx.get(key);
	}

}