package com.gnix.jflatgl.core.assets;

import com.gnix.jflatgl.core.audio.AudioChannel;
import com.gnix.jflatgl.core.audio.AudioMaster;
import com.gnix.jflatgl.core.audio.Sound;
import com.gnix.jflatgl.core.util.configuration.Settings;

import java.util.HashMap;
import java.util.Map;

public class Sfx {

	// Storing sounds
	private final Map<String, Sound> sfx = new HashMap<>();

	// SFX channels
	private final AudioChannel[] CHANNELS;
	public static final int MAIN_TRACK = 0;

	// Constructor initializes channel sources
	public Sfx() {
		int NUM_CHANNELS = 64;
		CHANNELS = new AudioChannel[NUM_CHANNELS];
		for (int i = 0; i < CHANNELS.length; ++i) {
			CHANNELS[i] = new AudioChannel(AudioMaster.generateSource());
			CHANNELS[i].setVolume(Settings.getSettingFloat("sfx_volume"));
		}
	}

	// INITIALIZATION
	public void addSfx(String key, Sound sound) {
		sfx.put(key, sound);
	}

	// RETRIEVING ASSETS
	public Sound getSfx(String key) {
		return sfx.get(key);
	}

	/**
	 * Check if a track is playing on any channel.
	 * @param sfxID The ID of the track to search for.
	 * @return True if the track is currently playing on any channel.
	 */
	public boolean isPlaying(String sfxID) {
		if (sfx.get(sfxID) == null) return false; // fail fast if the sfx doesn't exist
		for (AudioChannel channel : CHANNELS) {
			if (channel.getCurrentAudio() != null) { // skip non-playing channels
				if (channel.getCurrentAudio().getBufferID() == sfx.get(sfxID).getBufferID()) return true;
			}
		}
		return false;
	}

	/**
	 * Play the track on the first available channel.
	 * @param key The ID of the track to play.
	 * @return the index of the channel the track is playing on, or -1
	 * if there is no available channel, and thus the track does not play.
	 */
	public int playSfx(String key) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playSfx(i, key)) return i;
		}
		return -1;
	}

	/**
	 * Play the track on a specific channel.
	 * @param channel The channel to play the track on.
	 * @param key The ID of the track to play.
	 * @return true if the channel is free, and the track begins playing,
	 * or false otherwise.
	 */
	public boolean playSfx(int channel, String key) {
		if (!CHANNELS[channel].isPlaying()) {
			CHANNELS[channel].playAudio(sfx.get(key));
			return true;
		}
		else return false;
	}

	/**
	 * Play the track on the first available channel.
	 * @param key The ID of the track to play.
	 * @param startTime The time to start playback from on the track.
	 * @return The index of the channel the track is playing on, or -1
	 * if there is no available channel, and thus the track does not play.
	 */
	public int playSfxFrom(String key, float startTime) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playSfxFrom(key, startTime, i)) return i;
		}
		return -1;
	}

	/**
	 * Play the track on the specified channel, from the given starting time.
	 * @param key The ID of the track to play.
	 * @param startTime The time to start playback from on the track.
	 * @param channel The index of the channel to play the track on.
	 * @return True of the specified channel is not currently playing a track,
	 * or false otherwise (and thus the track does not play).
	 */
	public boolean playSfxFrom(String key, float startTime, int channel) {
		if (!CHANNELS[channel].isPlaying()) {
			CHANNELS[channel].playAudioFrom(sfx.get(key), startTime);
			return true;
		}
		return false;
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
		return CHANNELS[channel].pause();
	}

	/**
	 * Resume playing the track on the specified channel.
	 * @param channel The channel to resume playing on.
	 */
	public boolean resume(int channel) {
		return CHANNELS[channel].resume();
	}

	/**
	 * Stop playing the track on the specified channel.
	 * @param channel The channel to stop playing on.
	 * @return True if the channel was playing and is now stopped, or false otherwise.
	 */
	public boolean stop(int channel) {
		return CHANNELS[channel].stop();
	}

	/**
	 * Stop playing the specified track, whatever channel it may be playing on.
	 * @param sfxID The ID of the track to find and stop.
	 * @return True if the track was playing on any channel and is now stopped, or false otherwise.
	 */
	public boolean stop(String sfxID) {
		if (sfx.get(sfxID) == null) return false; // fail fast if sfx does not exist
		for (AudioChannel channel : CHANNELS) {
			if (channel.getCurrentAudio() != null) {
				if (channel.getCurrentAudio().getBufferID() == sfx.get(sfxID).getBufferID()) {
					return channel.stop();
				}
			}
		}
		return false;
	}

	// MODIFYING CHANNELS
	public void setVolume(int channel, float volume, float time) {
		CHANNELS[channel].tweenVolume(volume, time);
	}

	public void setPitch(int channel, float pitch, float time) {
		CHANNELS[channel].tweenPitch(pitch, time);
	}

	// UPDATING
	public void update() {
		for (AudioChannel channel : CHANNELS) {
			channel.update();
		}
	}

}
