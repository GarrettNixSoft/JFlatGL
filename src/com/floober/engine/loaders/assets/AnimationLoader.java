package com.floober.engine.loaders.assets;

import com.floober.engine.animation.Animation;
import com.floober.engine.game.Game;
import com.floober.engine.loaders.Loader;
import com.floober.engine.renderEngine.renderers.LoadRenderer;
import com.floober.engine.renderEngine.textures.TextureSet;
import com.floober.engine.util.Globals;
import com.floober.engine.util.Logger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AnimationLoader extends AssetLoader {
	
	// create loader
	public AnimationLoader() {
		super();
		directory = Loader.getJSON("/assets/animations_directory.json");
	}

	@Override
	protected void loadRecursive() {
		throw new RuntimeException("Animations cannot be loaded recursively");
	}

	@Override
	protected void loadDirectory() {
		Globals.LOAD_STAGE = LoadRenderer.ANIMATIONS;
		// load files
		JSONObject json_animations = directory.getJSONObject("animations");
		JSONObject json_animation_sets = directory.getJSONObject("animation_sets");
		// report counts (animation sets counted as animations
		Globals.animationTotal = json_animations.keySet().size() + json_animation_sets.keySet().size();
		// PHASE 1: LOAD EVERY ANIMATION
		loadAnimations(json_animations);
		// PHASE 2: LOAD AND CREATE ANIMATION SETS
		loadAnimationSets(json_animation_sets);
	}

	private void loadAnimations(JSONObject json) {
		// parse the data
		Set<String> keys = json.keySet();
		for (String key : keys) {
			try {
				// report current asset
				LoadRenderer.reportCurrentAsset(key);
				// load asset
				Logger.logLoad("Loading animation: " + key);
				// for each key, get the texture array name and delay values
				JSONObject animationObj = json.getJSONObject(key);
				Animation animation = parseAnimation(animationObj);
				Game.getAnimations().addAnimation(key, animation);
				Globals.animationCount++;
//				RunGame.loadRenderer.render();
			} catch (Exception e) {
				System.out.println("Failed to load Animation [key=" + key + "], error: " + e.toString());
			}
		}
	}

	private void loadAnimationSets(JSONObject json) {
		// parse the data
		Set<String> keys = json.keySet();
		for (String key : keys) {
			try {
				// report current asset
				LoadRenderer.reportCurrentAsset(key);
				// load asset
				Logger.logLoad("Loading animation set: " + key);
				JSONObject setObject = json.getJSONObject(key);
				// create the hashmap and add the animation data
				HashMap animationSet = parseAnimationSet(setObject);
				Game.getAnimations().addAnimationSet(key, animationSet);
				Logger.logLoadSuccess(key + ", with " + animationSet.size() + " animations");
				Globals.animationCount++;
//				RunGame.loadRenderer.render();
			} catch (Exception e) {
				System.out.println("Failed to load Animation Set [key=" + key + "], error: " + e.toString());
			}
		}
	}

	// PARSERS
	private Animation parseAnimation(JSONObject object) {
		String texArrayName = object.getString("textures");
		int delay = object.getInt("delay");
		TextureSet textures = Game.getTextureSet(texArrayName);
		return new Animation(textures, delay);
	}

	private HashMap<String, Animation> parseAnimationSet(JSONObject object) {
		Set<String> animations = object.keySet();
		HashMap<String, Animation> animationSet = new HashMap<>();
		for (String name : animations) {
			Animation ani = Game.getAnimations().getAnimation(object.getString(name));
			if (ani == null) {
				Logger.logError("ANIMATION " + object.getString(name) + " RETURNED NULL FROM ANIMATION BANK, ABORT LOAD");
				System.exit(-1);
			}
			animationSet.put(name, ani);
		}
		return animationSet;
	}
}