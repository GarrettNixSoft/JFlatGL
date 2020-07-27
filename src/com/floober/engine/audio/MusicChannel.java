package com.floober.engine.audio;

import com.floober.engine.util.Logger;
import com.floober.engine.util.time.Tweener;

public class MusicChannel {

	// playing audio
	private final Source source;

	// controlling settings
	private float volume;
	private float pitch;

	// timers for tweening
	private final Tweener volumeTweener;
	private final Tweener pitchTweener;

	/**
	 * Create a new Audio Channel.
	 * @param source The source for the channel to play audio through.
	 */
	public MusicChannel(Source source) {
		this.source = source;
		volumeTweener = new Tweener();
		pitchTweener = new Tweener();
		volume = pitch = 1;
	}

	// GETTERS
	public float getVolume() {
		return volume;
	}
	public float getPitch() {
		return pitch;
	}

	public boolean isPlaying() {
		return source.isPlaying();
	}

	// SETTERS
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setLooping(boolean looping) {
		source.setLooping(looping);
	}

	// PLAYING/PAUSING AUDIO
	public void playMusic(Sound sound) {
		source.play(sound.getBufferID());
	}

	/**
	 * Pause the track playing on this channel.
	 * @return True if this channel was playing and is now paused, or false otherwise.
	 */
	public boolean pause() {
		if (source.isPlaying()) {
			source.pause();
			return true;
		}
		else return false;
	}

	/**
	 * Resume playing the track on this channel.
	 * @return True if this channel was not playing and has now resumed, or false otherwise.
	 */
	public boolean resume() {
		if (!source.isPlaying()) {
			source.resume();
			return true;
		}
		else return false;
	}

	// CHANGING VOLUME

	/**
	 * Change the volume of this channel to the given value over the
	 * specified period of time.
	 * @param target The target volume.
	 * @param time The transition time, in seconds.
	 */
	public void tweenVolume(float target, float time) {
		volumeTweener.setTime(time);
		volumeTweener.setTargetValues(volume, target);
		volumeTweener.start();
	}

	/**
	 * Change the pitch of this channel to the given value over the
	 * specified period of time.
	 * @param target The target pitch.
	 * @param time The transition time, in seconds.
	 */
	public void tweenPitch(float target, float time) {
		pitchTweener.setTime(time);
		pitchTweener.setTargetValues(volume, target);
		pitchTweener.start();
	}

	// UPDATING

	/**
	 * Updates this channel. If volume or pitch tweens are in progress,
	 * they will be updated here.
	 */
	public void update() {
		// check volume change
//		if (volumeTweener.started()) {
//			volume = volumeTweener.getValue();
//			source.setVolume(volume);
//			if (volumeTweener.finished()) {
//				volumeTweener.reset();
//			}
//		}
		// check pitch change
		if (pitchTweener.started()) {
			pitch = pitchTweener.getValue();
			source.setPitch(pitch);
			if (pitchTweener.finished()) {
				pitchTweener.reset();
			}
		}
	}

}