package com.floober.engine.loaders.object;

import com.floober.engine.background.Background;
import com.floober.engine.background.BackgroundBase;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import org.json.JSONObject;

public class BackgroundLoader {

	// TODO maybe delete this class, I like the old self-loading background method

	public Background load(String path) {
		// get file
		JSONObject json = Loader.getJSON(path);
		// get base
		JSONObject baseObj = json.getJSONObject("base");
		// parse base
		String texID = baseObj.getString("tex_name");
		int animationTime;
		if (baseObj.getString("type").equals("animated"))
			animationTime = baseObj.getInt("animation_time");
		else animationTime = -1;
		float moveSpeed = baseObj.getFloat("move");
		// create base object
		BackgroundBase base = new BackgroundBase(Game.getTextureSet(texID), animationTime, moveSpeed);
		// TODO: parse elements and add them
		// create Background
		Background background = new Background(path);
		background.setBase(base);
		// return the result
		return background;
	}
}
