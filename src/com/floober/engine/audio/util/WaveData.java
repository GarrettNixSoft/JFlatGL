package com.floober.engine.audio.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

// Copied from an LWJGL 2.9.something project.
// Modified slightly to include a boolean for whether the com.floober.ddpc2.audio is stereo or mono.

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class WaveData {

	public final ByteBuffer data;
	public final int format;
	public final int samplerate;
	public final boolean stereo;

	private WaveData(ByteBuffer data, int format, int samplerate, boolean stereo) {
		this.data = data;
		this.format = format;
		this.samplerate = samplerate;
		this.stereo = stereo;
	}

	public void dispose() {
		this.data.clear();
	}

	public static WaveData create(URL path) {
		try {
			return create(AudioSystem.getAudioInputStream(new BufferedInputStream(path.openStream())));
		} catch (Exception var2) {
//			LWJGLUtil.log("Unable to create from: " + path);
			var2.printStackTrace();
			return null;
		}
	}

	public static WaveData create(String path) {
		return create(WaveData.class.getClassLoader().getResource(path));
	}

	public static WaveData create(InputStream is) {
		try {
			return create(AudioSystem.getAudioInputStream(is));
		} catch (Exception var2) {
//			LWJGLUtil.log("Unable to create from inputstream");
			var2.printStackTrace();
			return null;
		}
	}

	public static WaveData create(byte[] buffer) {
		try {
			return create(AudioSystem.getAudioInputStream(new BufferedInputStream(new ByteArrayInputStream(buffer))));
		} catch (Exception var2) {
			var2.printStackTrace();
			return null;
		}
	}

	public static WaveData create(ByteBuffer buffer) {
		try {
//			byte[] bytes = null;
			byte[] bytes;
			if (buffer.hasArray()) {
				bytes = buffer.array();
			} else {
				bytes = new byte[buffer.capacity()];
				buffer.get(bytes);
			}

			return create(bytes);
		} catch (Exception var2) {
			var2.printStackTrace();
			return null;
		}
	}

	public static WaveData create(AudioInputStream ais) {
		AudioFormat audioformat = ais.getFormat();
//		int channels = false;
		short channels;
		boolean stereo;
		if (audioformat.getChannels() == 1) {
			if (audioformat.getSampleSizeInBits() == 8) {
				channels = 4352;
			} else {
				if (audioformat.getSampleSizeInBits() != 16) {
					throw new RuntimeException("Illegal sample size");
				}

				channels = 4353;
			}
			stereo = false;
		} else {
			if (audioformat.getChannels() != 2) {
				throw new RuntimeException("Only mono or stereo is supported");
			}
			stereo = true;

			if (audioformat.getSampleSizeInBits() == 8) {
				channels = 4354;
			} else {
				if (audioformat.getSampleSizeInBits() != 16) {
					throw new RuntimeException("Illegal sample size");
				}

				channels = 4355;
			}
		}

		byte[] buf = new byte[audioformat.getChannels() * (int)ais.getFrameLength() * audioformat.getSampleSizeInBits() / 8];
//		int read = false;
		int total = 0;

		int read;
		try {
			while((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length) {
				total += read;
			}
		} catch (IOException var10) {
			return null;
		}

		ByteBuffer buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
		WaveData wavedata = new WaveData(buffer, channels, (int)audioformat.getSampleRate(), stereo);

		try {
			ais.close();
		} catch (IOException var9) {
		}

		return wavedata;
	}

	private static ByteBuffer convertAudioBytes(byte[] audio_bytes, boolean two_bytes_data) {
		ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
		dest.order(ByteOrder.nativeOrder());
		ByteBuffer src = ByteBuffer.wrap(audio_bytes);
		src.order(ByteOrder.LITTLE_ENDIAN);
		if (two_bytes_data) {
			ShortBuffer dest_short = dest.asShortBuffer();
			ShortBuffer src_short = src.asShortBuffer();

			while(src_short.hasRemaining()) {
				dest_short.put(src_short.get());
			}
		} else {
			while(src.hasRemaining()) {
				dest.put(src.get());
			}
		}

		dest.rewind();
		return dest;
	}
}

