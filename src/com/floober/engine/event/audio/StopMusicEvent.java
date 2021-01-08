package com.floober.engine.event.audio;

import com.floober.engine.game.Game;

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
