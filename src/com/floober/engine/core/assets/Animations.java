package com.floober.engine.core.assets;

import com.floober.engine.animation.Animation;
import com.floober.engine.animation.GlitchAnimation;

import java.util.HashMap;

/**
 * The Animations class stores data about Animations in HashMaps
 * that can be retrieved when needed. When an Animation is
 * retrieved, a new copy of the requested Animation is made and
 * returned to ensure every calling object has its own unique
 * instance of the stored animation data.
 */
public class Animations {

	// animation data
	private final HashMap<String, Animation> animations;
	private final HashMap<String, HashMap<String, Animation>> animationSets;

	// glitch animation data
	private final HashMap<String, GlitchAnimation> glitchAnimations;
	private final HashMap<String, HashMap<String, GlitchAnimation>> glitchAnimationSets;

	/**
	 * Create a new Animations cache to store animation data
	 * during the initial asset loading stage.
	 */
	public Animations() {
		animations = new HashMap<>();
		animationSets = new HashMap<>();
		glitchAnimations = new HashMap<>();
		glitchAnimationSets = new HashMap<>();
	}

	/**
	 * Add an animation to the cache.
	 * @param key The name that will be used to retrieve copies of this animation.
	 * @param animation The animation to store.
	 */
	public void addAnimation(String key, Animation animation) {
		animations.put(key, animation);
	}

	/**
	 * Add a set of animations to the cache.
	 * @param key The name that will be used to retrieve this animation set.
	 * @param animationSet The animation set to store.
	 */
	public void addAnimationSet(String key, HashMap<String, Animation> animationSet) {
		animationSets.put(key, animationSet);
	}
	/**
	 * Add a glitch animation to the cache.
	 * @param key The name that will be used to retrieve copies of this glitch animation.
	 * @param animation The glitch animation to store.
	 */
	public void addGlitchAnimation(String key, GlitchAnimation animation) {
		glitchAnimations.put(key, animation);
	}

	/**
	 * Add a set of glitch animations to the cache.
	 * @param key The name that will be used to retrieve this animation set.
	 * @param animationSet The animation set to store.
	 */
	public void addGlitchAnimationSet(String key, HashMap<String, GlitchAnimation> animationSet) {
		glitchAnimationSets.put(key, animationSet);
	}

	/**
	 * Get a copy of an animation stored in the cache.
	 * @param animationId The ID of the desired animation.
	 * @return A new copy of the animation stored with the given ID.
	 */
	public Animation getAnimation(String animationId) {
		Animation copy = new Animation();
		Animation original = animations.get(animationId);
		copy.setFrames(original.getFrames());
		copy.setFrameTime(original.getFrameTime());
		return copy;
	}

	/**
	 * Get a copy of a glitch animation stored in the cache.
	 * @param animationId The ID of the desired animation.
	 * @return A new copy of the animation stored with the given ID.
	 */
	public GlitchAnimation getGlitchAnimation(String animationId) {
		GlitchAnimation original = glitchAnimations.get(animationId);
		return new GlitchAnimation(original);
	}

	/**
	 * Get a set of animations stored in the cache.
	 * @param setId The ID of the desired animation set.
	 * @return The animation set with the given ID, or {@code null} if no animation set with the given ID exists.
	 */
	public HashMap<String, Animation> getAnimationSet(String setId) {
		return animationSets.get(setId);
	}

	/**
	 * Get a set of glitch animations stored in the cache.
	 * @param setId The ID of the desired animation set.
	 * @return The animation set with the given ID, or {@code null} if no animation set with the given ID exists.
	 */
	public HashMap<String, GlitchAnimation> getGlitchAnimationSet(String setId) {
		return glitchAnimationSets.get(setId);
	}

}