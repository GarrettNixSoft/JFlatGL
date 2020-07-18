package com.floober.engine.util.tests;

import com.floober.engine.audio.AudioMaster;
import com.floober.engine.audio.Sound;
import com.floober.engine.audio.Source;

import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL10.alDistanceModel;

@SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
public class AudioTest {

	public static void main(String[] args) throws Exception {
		AudioMaster.init();
		AudioMaster.setListenerData(0, 0, 0);
		alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);

		Sound sound = AudioMaster.loadSound("sfx/jump_1.wav");
		int buffer = sound.getBufferID();
		Source source = new Source();
		source.setLooping(true);
		source.play(buffer);

		float zPos = 0;
		source.setPosition(0, 0, 0);

		char c = ' ';
		while (c != 'q') {
			zPos -= 0.02f;
			source.setPosition(0, 0, zPos);
			System.out.print("\rxPos: " + zPos);
			Thread.sleep(10);
		}

		source.delete();
		AudioMaster.cleanUp();
	}

}