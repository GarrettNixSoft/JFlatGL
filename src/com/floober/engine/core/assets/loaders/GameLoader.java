package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.assets.loaders.gameassets.*;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.configuration.Config;

import java.util.ArrayList;
import java.util.List;

public class GameLoader extends Thread {
	// loading stages
	public static final int FONTS = 0, TEXTURES = 1, ANIMATIONS = 2, SFX = 3, MUSIC = 4;

	public static boolean LOAD_COMPLETE = false;

	// LOADING
	// stage
	public static int LOAD_STAGE = -1;
	// counters
	public static int fontCount;
	public static int fontTotal;
	public static int texCount;
	public static int texTotal;
	public static int animationCount;
	public static int animationTotal;
	public static int sfxCount;
	public static int sfxTotal;
	public static int musicCount;
	public static int musicTotal;
	public static String currentAsset = "";

	private final List<AssetLoader> loaders = new ArrayList<>();

	public GameLoader() {
		// fade in the loading screen
//		animateLoadRenderer();
		// begin the loading process
		AssetLoader.Mode mode = AssetLoader.Mode.DIRECTORY;
		loaders.add(new FontLoader());
		loaders.add(new TextureLoader());
		loaders.add(new AnimationLoader());
		loaders.add(new SfxLoader());
		loaders.add(new MusicLoader());
		// TODO implement this per project if you need more asset types to load
		// set modes
		for (AssetLoader assetLoader : loaders) {
			assetLoader.setMode(mode);
		}
	}

	@Override
	public void run() {
		// perform all load tasks that don't need the OpenGL context
		Logger.log("RUNNING GAME LOADER THREAD");
		load();
		Logger.log("GAME LOADER THREAD FINISHED, SLEEPING FOR 5 SECONDS");

		// TEST
		try {
			Thread.sleep((long) (Config.SPLASH_FAKE_LATENCY * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// END_TEST

		LOAD_COMPLETE = true;
	}

	public void finish() {
		for (AssetLoader assetLoader : loaders) {
			assetLoader.finish();
		}

		Logger.log("Loaded " + texTotal + " textures and " + fontTotal + " fonts");
	}

	public void load() {
		for (AssetLoader loader : loaders) {

			long start = System.nanoTime();

			loader.load();

			long elapsed = (System.nanoTime() - start) / 1_000_000;
			Logger.log("Ran loader " + loader.getClass().getName() + " in " + elapsed + "ms");

		}
//		animateEndLoadRenderer();
		// Loading process done; put something fancy in the log to make this point easy to find
		Logger.log("**************************************");
		Logger.log("*                                    *");
		Logger.log("*    ****    LOADING DONE    ****    *");
		Logger.log("*                                    *");
		Logger.log("**************************************");
	}

	public static String getLoadString() {

		StringBuilder result = new StringBuilder();
		result.append("Loading ");

		switch (LOAD_STAGE) {
			case FONTS -> {
				result.append("Fonts: [");
				result.append(fontCount).append(" / ").append(fontTotal).append(']');
			}
			case TEXTURES -> {
				result.append("Textures: [");
				result.append(texCount).append(" / ").append(texTotal).append(']');
			}
			case ANIMATIONS -> {
				result.append("Animations: [");
				result.append(animationCount).append(" / ").append(animationTotal).append(']');
			}
			case SFX -> {
				result.append("SFX: [");
				result.append(sfxCount).append(" / ").append(sfxTotal).append(']');
			}
			case MUSIC -> {
				result.append("Music: [");
				result.append(musicCount).append(" / ").append(musicTotal).append(']');
			}
			default -> {}
		}

		return result.toString();

	}

//	private void animateLoadRenderer() {
//		long start = System.nanoTime();
//		Globals.LOAD_STAGE = LoadRenderer.FADE_IN;
//		while (TimeScale.getScaledTime(start) < 1000) {
//			RunGame.loadRenderer.render();
//		}
//	}

//	private void animateEndLoadRenderer() {
//		long start = System.nanoTime();
//		// FADE IN CHECK MARK
//		Globals.LOAD_STAGE = LoadRenderer.DONE;
//		while (TimeScale.getScaledTime(start) < 1000) {
//			RunGame.loadRenderer.render();
//		}
//		// FADE OUT LOAD BAR
//		Globals.LOAD_STAGE = LoadRenderer.FADE_OUT;
//		while (TimeScale.getScaledTime(start) < 2000) {
//			RunGame.loadRenderer.render();
//		}
//	}

}
