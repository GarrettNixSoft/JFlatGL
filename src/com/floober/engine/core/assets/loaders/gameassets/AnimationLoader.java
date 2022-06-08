package com.floober.engine.core.assets.loaders.gameassets;

import com.floober.engine.animation.Animation;
import com.floober.engine.animation.GlitchAnimation;
import com.floober.engine.core.assets.loaders.AssetLoader;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.renderers.LoadRenderer;
import com.floober.engine.core.renderEngine.textures.TextureSet;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.file.FileUtil;
import com.floober.engine.test.RunGame;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AnimationLoader extends AssetLoader {

	// create loader
	public AnimationLoader() {
		directory = FileUtil.getJSON("/assets/animations_directory.json");
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
				if (animationObj.has("glitch_animation")) {
					GlitchAnimation animation = parseGlitchAnimation(animationObj);
					Game.getAnimations().addGlitchAnimation(key, animation);
				}
				else {
					Animation animation = parseAnimation(animationObj);
					Game.getAnimations().addAnimation(key, animation);
				}
				Globals.animationCount++;
				// render the load screen
				LoadRenderer.instance.render();
			} catch (Exception e) {
				System.out.println("Failed to load Animation [key=" + key + "], error: " + e);
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
				if (key.endsWith("glitch")) {
					HashMap glitchSet = parseGlitchAnimationSet(setObject);
					Game.getAnimations().addGlitchAnimationSet(key, glitchSet);
					Logger.logLoadSuccess(key + ", with " + glitchSet.size() + " glitch animations");
				}
				else {
					// create the hashmap and add the animation data
					HashMap animationSet = parseAnimationSet(setObject);
					Game.getAnimations().addAnimationSet(key, animationSet);
					Logger.logLoadSuccess(key + ", with " + animationSet.size() + " animations");
				}
				Globals.animationCount++;
				// render the load screen
				LoadRenderer.instance.render();
			} catch (Exception e) {
				System.out.println("Failed to load Animation Set [key=" + key + "], error: " + e);
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

	private GlitchAnimation parseGlitchAnimation(JSONObject object) {
		Animation base = parseAnimation(object);
		String glitchTexArrayName = object.getString("glitch_textures");
		TextureSet glitchTextures = Game.getTextureSet(glitchTexArrayName);
		int glitchDelay = object.getInt("glitch_delay");
		GlitchAnimation animation = new GlitchAnimation(base, glitchTextures);
		animation.setGlitchDelay(glitchDelay);
		return animation;
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

	private HashMap<String, GlitchAnimation> parseGlitchAnimationSet(JSONObject object) {
		Set<String> animations = object.keySet();
		HashMap<String, GlitchAnimation> animationSet = new HashMap<>();
		for (String name : animations) {
			GlitchAnimation ani = Game.getAnimations().getGlitchAnimation(object.getString(name));
			if (ani == null) {
				Logger.logError("GLITCH ANIMATION " + object.getString(name) + " RETURNED NULL FROM ANIMATION BANK, ABORT LOAD");
				System.exit(-1);
			}
			animationSet.put(name, ani);
		}
		return animationSet;
	}

	@Override
	public void finish() {
		// nothing
	}
}