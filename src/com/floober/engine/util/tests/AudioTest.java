package com.floober.engine.util.tests;

import com.floober.engine.audio.AudioChannel;
import com.floober.engine.audio.AudioMaster;
import com.floober.engine.audio.Sound;
import com.floober.engine.audio.Source;

import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL10.alDistanceModel;

public class AudioTest {

	public static void main(String[] args) throws Exception {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);

		Source source = AudioMaster.generateSource();
		source.setLooping(true);
		AudioChannel channel = new AudioChannel(source);
		Sound sound = AudioMaster.loadSound("sfx/jump_1.wav");
		channel.playAudio(sound);

		float zPos = 0;
		source.setPosition(0, 0, 0);

		char c = (char) System.in.read();
		while (c != 'q') {
			if (c == 't') {
				channel.tweenVolume(0, 2);
				c = ' ';
			}
			if (c == 'p') {
				channel.tweenPitch(2, 2);
				c = ' ';
			}
			channel.update();
			Thread.sleep(10);
		}

		AudioMaster.cleanUp();
	}

}