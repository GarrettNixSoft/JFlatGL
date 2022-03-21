package com.floober.engine.core.audio.event;

import com.floober.engine.core.assets.Music;
import com.floober.engine.core.Game;
import com.floober.engine.core.util.Logger;

public class FadeMusicEvent extends AudioEvent {

	private final int channel;
	private final float targetVol;
	private final float fadeTime;

	public FadeMusicEvent(float targetVol, float fadeTime) {
		super(true, "");
		this.channel = Music.MAIN_TRACK;
		this.targetVol = targetVol;
		this.fadeTime = fadeTime;
	}

	public FadeMusicEvent(int channel, float targetVol, float fadeTime) {
		super(true, "");
		this.channel = channel;
		this.targetVol = targetVol;
		this.fadeTime = fadeTime;
	}

	@Override
	public void onStart() {
		Logger.logEvent("Fading music on channel " + channel + " to " + targetVol + " over " + fadeTime + "s");
		Game.fadeMusic(channel, targetVol, fadeTime);
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
