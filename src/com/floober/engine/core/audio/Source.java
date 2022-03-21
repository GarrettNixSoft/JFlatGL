package com.floober.engine.core.audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

import static org.lwjgl.openal.AL10.*;

public class Source {

	private final int sourceID;

	private Sound currentSound;

	public Source() {
		sourceID = alGenSources();
		alSourcef(sourceID, AL_ROLLOFF_FACTOR, 10);
		alSourcef(sourceID, AL_REFERENCE_DISTANCE, 2);
		alSourcef(sourceID, AL_MAX_DISTANCE, 50);
	}

	// GETTERS
	public boolean isPlaying() {
		return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
	}

	public Sound getCurrentSound() {
		return currentSound;
	}

	// SETTERS
	public void setPosition(Vector3f position) {
		alSource3f(sourceID, AL_POSITION, position.x, position.y, position.z);
	}
	public void setPosition(float x, float y, float z) {
		alSource3f(sourceID, AL_POSITION, x, y, z);
	}
	public void setVelocity(Vector3f velocity) {
		alSource3f(sourceID, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	public void setVelocity(float x, float y, float z) {
		alSource3f(sourceID, AL_VELOCITY, x, y, z);
	}
	public void setLooping(boolean loop) {
		alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
	}

	public void setVolume(float volume) {
		alSourcef(sourceID, AL_GAIN, volume);
	}

	public void setPitch(float pitch) {
		alSourcef(sourceID, AL_PITCH, pitch);
	}

	// ACTIONS
	public void play(Sound sound) {
		stop();
		this.currentSound = sound;
		alSourcei(sourceID, AL_BUFFER, sound.getBufferID());
		alSourcePlay(sourceID);
	}

	public void pause() {
		alSourcePause(sourceID);
	}

	public void resume() {
		alSourcePlay(sourceID);
	}

	public void stop() {
		alSourceStop(sourceID);
	}

	public boolean setPosition(float position) {
		position %= currentSound.getLength();
		AL10.alSourcef(sourceID, AL11.AL_SEC_OFFSET, position);
		return AL10.alGetError() == 0;
	}

	public void delete() {
		stop();
		alDeleteSources(sourceID);
	}

	// updating
	public void update() {
		// null out currentSound if no sound is playing
		if (currentSound != null) {
			if (!isPlaying()) currentSound = null;
		}
	}

}