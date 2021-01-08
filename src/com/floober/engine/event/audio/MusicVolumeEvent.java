package com.floober.engine.event.audio;

import com.floober.engine.game.Game;

public class MusicVolumeEvent extends AudioEvent {

	private final float targetVolume;

	public MusicVolumeEvent(float targetVolume) {
		super(false, "");
		this.targetVolume = targetVolume;
	}

	@Override
	public void onStart() {
		Game.getMusic().setCurrentMusicVolume(targetVolume);
		complete = true;
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void onFinish() {
		// nothing
	}
}
