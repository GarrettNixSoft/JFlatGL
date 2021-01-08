package com.floober.engine.background;

import com.floober.engine.game.Game;
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
	public Background() {
		elements = new ArrayList<>();
	}

	public void setBase(BackgroundBase base) {
		this.base = base;
	}

	public void addElement(BackgroundElement element) {
		elements.add(element);
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