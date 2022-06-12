package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.assets.loaders.gameassets.*;
import com.floober.engine.core.splash.SplashRenderer;
import com.floober.engine.core.util.Globals;
import com.floober.engine.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameLoader extends Thread {
	// loading stages
	public static final int FONTS = 0, TEXTURES = 1, ANIMATIONS = 2, SFX = 3, MUSIC = 4, FADE_IN = 5, DONE = 6, FADE_OUT = 7;

	public static boolean LOAD_COMPLETE = false;

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
			Thread.sleep(5000);
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

		Logger.log("Loaded " + Globals.texTotal + " textures and " + Globals.fontTotal + " fonts");
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
