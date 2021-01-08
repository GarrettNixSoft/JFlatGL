package com.floober.engine.event.audio;

import com.floober.engine.game.Game;
import com.floober.engine.util.Logger;
import com.floober.engine.util.time.TimeScale;

public class PlayEvent extends AudioEvent {

	public enum PlayMode {
		FULL, PARTIAL, LOOP
	}

	// music or sfx
	private final Type type;
	private final PlayMode mode;

	// duration
	private final float duration;
	private final float startTime;
	private long start;

	public PlayEvent(String audioID, Type type, PlayMode mode, float startTime, float duration) {
		super(false, audioID);
		this.type = type;
		this.mode = mode;
		this.startTime = startTime;
		this.duration = duration;
	}

	@Override
	public void onStart() {
		Logger.logEvent("Now playing " + audioID + " (type " + type + ") from " + startTime + "s");
		switch (type) {
			case SFX -> Game.playSfx(audioID);
			case MUSIC -> {
				if (mode == PlayMode.FULL)
					Game.playMusic(audioID);
				else if (mode == PlayMode.LOOP)
					Game.loopMusic(audioID);
				else
					Game.playMusic(audioID);
			}
		}
		start = System.nanoTime();
	}

	@Override
	public void update() {
		if (mode == PlayMode.FULL) {
			switch (type) {
				case SFX -> complete = !Game.getSfx().isPlaying(audioID);
				case MUSIC -> complete = !Game.getMusic().isPlaying(audioID);
			}
		}
		else if (mode == PlayMode.PARTIAL) {
			long elapsed = TimeScale.getRawTime(start);
			complete = (elapsed / 1000.0) > duration;
		}
		else {
			complete = true;
		}
	}

	@Override
	public void onFinish() {
		if (type == Type.SFX) Game.getSfx().stop(audioID);
	}
}
