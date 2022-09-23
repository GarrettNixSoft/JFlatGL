package com.gnix.jflatgl.core.audio;

import com.gnix.jflatgl.core.assets.loaders.Loader;
import com.gnix.jflatgl.core.audio.util.WaveData;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.file.FileUtil;
import com.gnix.jflatgl.core.util.file.ResourceLoader;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import java.io.IOException;
import java.io.InputStream;
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
	private static final List<Source> sources = new ArrayList<>();

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
		Logger.logLoad("Loading sound: " + file);
		// create an input stream
		try (InputStream inputStream = Loader.tryGetInputStream(file)) {
			long start = System.nanoTime();
			int buffer = alGenBuffers();
			buffers.add(buffer);
			// load the input stream
			WaveData waveFile = WaveData.create(inputStream);
			alBufferData(buffer, waveFile.format(), waveFile.data(), waveFile.samplerate());
			Sound sound = new Sound(buffer, waveFile.stereo());
			waveFile.dispose();
			long elapsed = (System.nanoTime() - start) / 1_000_000;
			Logger.logLoad("Loaded sound file in " + elapsed + "ms");
			return sound;
		} catch (IOException e) {
			Logger.logError("Error loading " + file + ": " + e.getMessage(), Logger.CRITICAL);
			return null;
		}
//		file = file.replace("/", "\\");
	}

	private static InputStream tryGetInputStream(String path) {
		// declare the input stream
		InputStream in = null;
		// open the stream; how this executes depends on whether the game is running in-IDE or as an exported JAR
		try {
			in = ResourceLoader.getResourceAsStream(path); // exported game
		}
		catch (Exception e1) {
			try {
				Logger.logLoadError("Export-load failed, trying in-development load...");
				in = ResourceLoader.getResourceAsStream("res" + FileUtil.SEPARATOR + path); // in development
			} catch (Exception e2) {
				Logger.logLoadError("In-development load failed. Resource could not be found.");
			}
		}
		Logger.logLoad("Load success!");
		return in;
	}

	public static Source generateSource() {
		Source source = new Source();
		sources.add(source);
		return source;
	}

	public static void cleanUp() {
		for (int buffer : buffers) {
			alDeleteBuffers(buffer);
		}
		for (Source source : sources) {
			source.delete();
		}
		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
	}

}
