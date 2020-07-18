package com.floober.engine.audio;

import com.floober.engine.audio.util.WaveData;
import com.floober.engine.util.Logger;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class AudioMaster {

	public static long device;
	public static long context;

	private static final List<Integer> buffers = new ArrayList<>();

	/**
	 * Initialize OpenAL for the current context.
	 */
	public static void init() {
		device = alcOpenDevice((ByteBuffer) null);
		ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
		context = alcCreateContext(device, (IntBuffer) null);
		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCapabilities);
	}

	public static void setListenerData(Vector3f position) {
		alListener3f(AL_POSITION, position.x, position.y, position.z);
		alListener3f(AL_VELOCITY, 0, 0, 0);
	}

	public static void setListenerData(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
		alListener3f(AL_VELOCITY, 0, 0, 0);
	}

	public static Sound loadSound(String file) {
		long start = System.nanoTime();
		int buffer = alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		Sound sound = new Sound(buffer, waveFile.stereo);
		waveFile.dispose();
		long elapsed = (System.nanoTime() - start) / 1_000_000;
		Logger.logLoad("Loaded sound file in " + elapsed + "ms");
		return sound;
	}

	public static void cleanUp() {
		for (int buffer : buffers) {
			alDeleteBuffers(buffer);
		}
		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
	}

}