package com.floober.engine.core.assets.loaders.gameassets;

import com.floober.engine.animation.Animation;
import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.Game;
import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.gameassets.temp.RawAnimation;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.file.FileUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AnimationLoader extends AssetLoader {

	private final HashMap<String, RawAnimation> rawAnimations;

	private final HashMap<String, HashMap<String, RawAnimation>> rawAnimationSets;

	// create loader
	public AnimationLoader() {
		directory = FileUtil.getJSON("/assets/animations_directory.json");
		rawAnimations = new HashMap<>();
		rawAnimationSets = new HashMap<>();
	}

	@Override
	protected void loadRecursive() {
		throw new RuntimeException("Animations cannot be loaded recursively");
	}

	@Override
	protected void loadDirectory() {
		GameLoader.LOAD_STAGE = GameLoader.ANIMATIONS;
		// load files
		JSONObject json_animations = directory.getJSONObject("animations");
		JSONObject json_animation_sets = directory.getJSONObject("animation_sets");
		// report counts (animation sets counted as animations
		GameLoader.animationTotal = json_animations.keySet().size() +
									json_animation_sets.keySet().size();
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
				GameLoader.currentAsset = key;
				// load asset
				Logger.logLoad("Loading animation: " + key);
				// for each key, get the texture array name and delay values
				JSONObject animationObj = json.getJSONObject(key);
				RawAnimation animation = parseAnimation(key, animationObj);
				rawAnimations.put(key, animation);
				GameLoader.animationCount++;
			} catch (Exception e) {
				Logger.logLoadError("Failed to load Animation [key=" + key + "], error: " + e);
			}
		}
	}

	private void loadAnimationSets(JSONObject json) {
		// parse the data
		Set<String> keys = json.keySet();
		for (String key : keys) {
			try {
				// report current asset
				GameLoader.currentAsset = key;
				// load asset
				Logger.logLoad("Loading animation set: " + key);
				JSONObject setObject = json.getJSONObject(key);
				// create the hashmap and add the animation data
				HashMap animationSet = parseAnimationSet(setObject);
				rawAnimationSets.put(key, animationSet);
				Logger.logLoadSuccess(key + ", with " + animationSet.size() + " animations");
				GameLoader.animationCount++;
			} catch (Exception e) {
				Logger.logLoadError("Failed to load Animation Set [key=" + key + "], error: " + e);
			}
		}
	}

	// PARSERS
	private RawAnimation parseAnimation(String key, JSONObject object) {
		String texArrayName = object.getString("textures");
		int delay = object.getInt("delay");
		return new RawAnimation(key, texArrayName, delay);
	}

	private HashMap<String, RawAnimation> parseAnimationSet(JSONObject object) {

		Set<String> animations = object.keySet();
		HashMap<String, RawAnimation> animationSet = new HashMap<>();
		for (String name : animations) {
			String key = object.getString(name);
			RawAnimation rawAnimation = rawAnimations.get(key);
			if (rawAnimation == null) {
				Logger.logError("ANIMATION " + object.getString(name) + " RETURNED NULL FROM ANIMATION BANK, ABORT LOAD");
				System.exit(-1);
			}
			animationSet.put(name, rawAnimation);
		}
		return animationSet;
	}

	@Override
	public void finish() {

		for (RawAnimation rawAnimation : rawAnimations.values()) {
			rawAnimation.addToGame();
		}

		for (String key : rawAnimationSets.keySet()) {

			HashMap<String, RawAnimation> rawAnimationSet = rawAnimationSets.get(key);

			HashMap<String, Animation> animationSet = new HashMap<>();
			for (String setKey : rawAnimationSet.keySet()) {
				animationSet.put(setKey, rawAnimationSet.get(setKey).convert());
			}

			Game.getAnimations().addAnimationSet(key, animationSet);

		}

	}
}