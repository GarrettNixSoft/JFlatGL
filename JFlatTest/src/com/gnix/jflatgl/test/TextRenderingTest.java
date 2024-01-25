package com.gnix.jflatgl.test;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.gameState.CustomStateManager;
import com.gnix.jflatgl.core.splash.SplashRenderer;

public class TextRenderingTest {

	public static void main(String[] args) {

		Game.loadConfig();

		Game.init(new SplashRenderer() {
			@Override
			public void init() {
				//
			}

			@Override
			public void render() {
				//
			}

			@Override
			public void cleanUp() {
				//
			}
		}, new CustomStateManager(TextRenderState.class));

		Game.runGameLoop();

		Game.cleanUp();

	}

}
