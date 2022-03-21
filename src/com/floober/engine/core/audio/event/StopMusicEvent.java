package com.floober.engine.core.audio.event;

import com.floober.engine.core.Game;

public class StopMusicEvent extends AudioEvent {

	public StopMusicEvent(String audioID) {
		super(false, audioID);
	}

	@Override
	public void onStart() {
		Game.getMusic().stop(audioID);
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
