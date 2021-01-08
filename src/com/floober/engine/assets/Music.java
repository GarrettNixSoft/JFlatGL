package com.floober.engine.assets;

import com.floober.engine.audio.AudioChannel;
import com.floober.engine.audio.AudioMaster;
import com.floober.engine.audio.Sound;
import com.floober.engine.util.Logger;

import java.util.HashMap;
import java.util.Map;

public class Music {

	// STORING MUSIC
	private final Map<String, Sound> music = new HashMap<>();

	// Music tracks
	private final AudioChannel[] CHANNELS;
	public static final int NUM_CHANNELS = 4;
	public static final int MAIN_TRACK = 0;
	private int currentMusicChannel;

	// Constructor initializes channel sources
	public Music() {
		CHANNELS = new AudioChannel[NUM_CHANNELS];
		for (int i = 0; i < CHANNELS.length; ++i) {
			CHANNELS[i] = new AudioChannel(AudioMaster.generateSource());
		}
	}

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

	/**
	 * Check if a track is playing on any channel.
	 * @param musicID The ID of the track to search for.
	 * @return True if the track is currently playing on any channel.
	 */
	public boolean isPlaying(String musicID) {
		for (AudioChannel channel : CHANNELS) {
			try {
				if (channel.getCurrentAudio().getBufferID() == music.get(musicID).getBufferID()) return true;
			}
			catch (NullPointerException e) {
				return false; // cannot be playing same track if nothing is playing
			}
		}
		return false;
	}

	// MUSIC CONTROL

	/**
	 * Get the index of the first available music channel. If all channels
	 * are busy, return -1.
	 * @return The channel index, or -1 if none are free.
	 */
	public int getNextChannel() {
		for (int i = 0; i < NUM_CHANNELS; i++) {
			if (!CHANNELS[i].isPlaying()) return i;
		}
		return -1;
	}

	/**
	 * Play the track on the first available channel.
	 * @param key The ID of the track to play.
	 * @return the index of the channel the track is playing on, or -1 if there is no available channel, and thus the track does not play.
	 */
	public int playMusic(String key) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playMusic(key, i)) return currentMusicChannel = i;
		}
		return -1;
	}

	/**
	 * Play the track on a specific channel.
	 * @param key The ID of the track to play.
	 * @param channel The channel to play the track on.
	 * @return true if the channel is free, and the track begins playing, or false otherwise.
	 */
	private boolean playMusic(String key, int channel) {
		if (!CHANNELS[channel].isPlaying()) {
			currentMusicChannel = channel;
			CHANNELS[channel].playAudio(music.get(key));
			return true;
		}
		else return false;
	}

	/**
	 * Loop the track on the first available channel.
	 * @param key The ID of the track to play.
	 * @return The index of the channel the track is playing on, or -1 if there are no free channels.
	 */
	public int loopMusic(String key) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playMusic(key, i)) {
				setLooping(i, true);
				Logger.logEvent("... on channel #" + i + ", with starting volume " + CHANNELS[i].getVolume());
				return currentMusicChannel = i;
			}
		}
		return -1;
	}

	/**
	 * Loop the track on a specified channel.
	 * @param key The ID of the track to play.
	 * @param channel The channel to play the track on.
	 * @return {@code true} if the channel is free, or {@code false} otherwise.
	 */
	private boolean loopMusic(String key, int channel) {
		if (!CHANNELS[channel].isPlaying()) {
			currentMusicChannel = channel;
			setLooping(channel, true);
			CHANNELS[channel].playAudio(music.get(key));
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
	public int playMusicFrom(String key, float startTime) {
		for (int i = 0; i < CHANNELS.length; ++i) {
			if (playMusicFrom(key, i, startTime)) return currentMusicChannel = i;
		}
		return -1;
	}

	/**
	 * Play the track on the specified channel, from the given starting time.
	 * @param key The ID of the track to play.
	 * @param channel The index of the channel to play the track on.
	 * @param startTime The time to start playback from on the track.
	 * @return True of the specified channel is not currently playing a track,
	 * or false otherwise (and thus the track does not play).
	 */
	private boolean playMusicFrom(String key, int channel, float startTime) {
		if (!CHANNELS[channel].isPlaying()) {
			currentMusicChannel = channel;
			CHANNELS[channel].playAudioFrom(music.get(key), startTime);
			return true;
		}
		return false;
	}

	/**
	 * Fade the most recent music track to zero volume while fading in
	 * a new track simultaneously on the first available channel. If no
	 * channels are free, nothing will happen.
	 * @param key The ID of the track to crossfade to.
	 * @param time The transition time of the crossfade.
	 * @return The index of the channel the new track is playing on, or -1 if no channels are free.
	 */
	public int crossFade(String key, float time) {
		for (int i = 0; i < NUM_CHANNELS; i++) {
			if (!CHANNELS[i].isPlaying()) {
				crossFade(key, i, time);
				return currentMusicChannel = i;
			}
		}
		return -1;
	}

	/**
	 * Fade the most recent music track to zero volume while fading in
	 * a new track simultaneously on the specified channel. If the specified
	 * channel is busy, nothing will happen.
	 * @param key The ID of the track to crossfade to.
	 * @param channel The channel to play the new track on.
	 * @param time The transition time of the crossfade.
	 * @return {@code true} if the specified channel is free, or {@code false} otherwise.
	 */
	private boolean crossFade(String key, int channel, float time) {
		if (!CHANNELS[channel].isPlaying()) {
			fadeMusic(currentMusicChannel, 0, time);
			setVolume(channel, 0, 0);
			playMusic(key, channel);
			currentMusicChannel = channel;
			return true;
		}
		else return false;
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
	 * @return True if the channel was not playing and now is, or false otherwise.
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
	 * @param musicID The ID of the track to find and stop.
	 * @return True if the track was playing on any channel and is now stopped, or false otherwise.
	 * <br>
	 * <br>
	 *     Note: if {@code musicID} is "ALL," then all tracks will be stopped, and this method will
	 *     return {@code true}.
	 */
	public boolean stop(String musicID) {
		if (musicID.equals("ALL")) {
			for (AudioChannel channel : CHANNELS) {
				channel.stop();
			}
			return true;
		}
		else {
			for (AudioChannel channel : CHANNELS) {
				if (channel.getCurrentAudio().getBufferID() == music.get(musicID).getBufferID()) {
					return channel.stop();
				}
			}
			return false;
		}
	}

	// MODIFYING CHANNELS
	public void setCurrentMusicVolume(float volume) {
		setVolume(currentMusicChannel, volume, 0);
	}

	public void setVolume(int channel, float volume, float time) {
		if (time == 0) {
			CHANNELS[channel].setVolume(volume);
		}
		else {
			CHANNELS[channel].tweenVolume(volume, time);
		}
	}

	public void setPitch(int channel, float pitch, float time) {
		if (time == 0) {
			CHANNELS[channel].setPitch(pitch);
		}
		else {
			CHANNELS[channel].tweenPitch(pitch, time);
		}
	}

	public void fadeMusic(int channel, float target, float time) {
		CHANNELS[channel].tweenVolume(target, time);
	}

	// UPDATING
	public void update() {
		for (AudioChannel channel : CHANNELS) {
			channel.update();
		}
	}

}