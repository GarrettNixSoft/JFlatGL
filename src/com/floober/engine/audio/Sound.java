package com.floober.engine.audio;

public class Sound {

	private final int bufferID;
	private final boolean isStereo;

	public Sound(int bufferID, boolean stereo) {
		this.bufferID = bufferID;
		this.isStereo = stereo;
	}

	public int getBufferID() {
		return bufferID;
	}

	public boolean isStereo() {
		return isStereo;
	}
}