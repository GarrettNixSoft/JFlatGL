package com.floober.engine.background;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.file.FileUtil;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Floober
 * 
 * The Background is composed of a single, looping
 * background image, and any number of elements on
 * top of it that may move as you see fit.
 * 
 */
public class Background {
	
	// game
	private Game game;
	
	// base
	private BackgroundBase base;
	
	// elements
	private List<BackgroundElement> elements;
	
	// build a background from a JSON configuration
	public Background(String jsonFile) {
		elements = new ArrayList<>();
		JSONObject json = FileUtil.getJSON(jsonFile);
		loadBase(json);
		loadElements(json);
	}

	public void setBase(BackgroundBase base) {
		this.base = base;
	}

	public void addElement(BackgroundElement element) {
		elements.add(element);
	}

	private void loadBase(JSONObject json) {
		JSONObject baseObj = json.getJSONObject("base");
		String texName = baseObj.getString("texture");
		float speed = baseObj.getFloat("speed");
		int animationTime = baseObj.optInt("animationTime", -1);
		TextureSet texData = Game.getTextures().getTextureSet(texName);
		base = new BackgroundBase(texData, animationTime, speed);
		System.out.println("[Background] Loaded base: " + texName);
	}
	
	private void loadElements(JSONObject json) {
		elements = new ArrayList<>();
		Set<String> keys = json.keySet();
		for (String key : keys) {
			if (key.equals("base")) continue;
			JSONObject elementObj = json.getJSONObject(key);
			String texName = elementObj.getString("texture");
			String moveType = elementObj.getString("movetype");
			if (moveType.equals("static")) {
				// TODO
			}
			else if (moveType.equals("fixed")) {
				// TODO
			}
		}
	}
	
	// update
	public void update() {
		base.update();
		for (BackgroundElement be : elements) be.update();
	}

	public void render() {
		base.render();
		for (BackgroundElement be : elements) be.render();
	}
}