package com.floober.engine.core.audio.event;

import com.floober.engine.event.QueuedEvent;

public abstract class AudioEvent extends QueuedEvent {

	protected String audioID;
	protected float duration;

	public enum Type {
		MUSIC, SFX
	}

	public AudioEvent(boolean blocking, String audioID) {
		super(blocking);
		this.audioID = audioID;
	}
}
