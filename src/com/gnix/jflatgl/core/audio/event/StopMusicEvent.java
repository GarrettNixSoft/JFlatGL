package com.gnix.jflatgl.core.audio.event;

import com.gnix.jflatgl.core.Game;

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
