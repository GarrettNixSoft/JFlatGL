package com.floober.engine.assets;

import com.floober.engine.animation.Animation;

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

	/**
	 * Create a new Animations cache to store animation data
	 * during the initial asset loading stage.
	 */
	public Animations() {
		animations = new HashMap<>();
		animationSets = new HashMap<>();
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
	 * Get a set of animations stored in the cache.
	 * @param setId The ID of the desired animation set.
	 * @return The animation set with the given ID, or {@code null} if no animation set with the given ID exists.
	 */
	public HashMap<String, Animation> getAnimationSet(String setId) {
		return animationSets.get(setId);
	}

}