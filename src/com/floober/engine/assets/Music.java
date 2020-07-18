package com.floober.engine.assets;

import com.floober.engine.audio.Sound;
import com.floober.engine.audio.Source;

import java.util.HashMap;
import java.util.Map;

public class Music {

	// Constructor initializes channel sources
	public Music() {
		int NUM_CHANNELS = 4;
		CHANNELS = new Source[NUM_CHANNELS];
		for (int i = 0; i < CHANNELS.length; ++i) {
			CHANNELS[i] = new Source();
		}
	}

	// STORING MUSIC
	private final Map<String, Sound> music = new HashMap<>();

	// LOADING MUSIC

	/**
	 * Add a track (Sound object) to the music store.
	 * @param key The key to access the track with.
	 * @param sound The track to store.
	 */
	public void addMusic(String key, Sound sound) {
		music.put(key, sound);
	}

	// RETRIEVING MUSIC

	/**
	 * Retrieve a track from the music store.
	 * @param key The key of the desired track.
	 * @return The track requested, or null if no such track exists.
	 */
	public Sound getMusic(String key) {
		return music.get(key);
	}

	// Music tracks
	private final Source[] CHANNELS;

	// MUSIC CONTROL

	/**
	 * Play the track on the first available channel.
	 * @param key The ID of the track to play.
	 * @return the index of the channel the track is playing on, or -1 if there is no available channel, and thus the track does not play.
	 */
	public int playMusic(String key) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playMusic(i, key)) return i;
		}
		return -1;
	}

	/**
	 * Play the track on a specific channel.
	 * @param channel The channel to play the track on.
	 * @param key The ID of the track to play.
	 * @return true if the channel is free, and the track begins playing, or false otherwise.
	 */
	public boolean playMusic(int channel, String key) {
		if (!CHANNELS[channel].isPlaying()) {
			CHANNELS[channel].play(music.get(key).getBufferID());
			return true;
		}
		else return false;
	}

	/**
	 * Resume playing the track on the specified channel.
	 * @param channel The channel to resume playing on.
	 */
	public void play(int channel) {
		CHANNELS[channel].resume();
	}

	/**
	 * Set whether the specified channel should loop its track.
	 * @param channel The channel to modify.
	 * @param looping Whether or not to loop the track.
	 */
	public void setLooping(int channel, boolean looping) {
		CHANNELS[channel].setLooping(looping);
	}

	/**
	 * Pause the track playing on the specified channel.
	 * @param channel The channel to pause.
	 * @return True if the channel was playing and is now paused, or false otherwise.
	 */
	public boolean pauseTrack(int channel) {
		if (CHANNELS[channel].isPlaying()) {
			CHANNELS[channel].pause();
			return true;
		}
		else return false;
	}



}