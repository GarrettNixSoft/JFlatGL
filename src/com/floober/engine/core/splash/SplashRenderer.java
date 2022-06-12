package com.floober.engine.core.splash;

/**
 * Define what should be rendered
 */
public abstract class SplashRenderer {

	/**
	 * Initialize any objects necessary. This will be called automatically
	 * by the SplashRenderer when the GameLoader passes your SplashRenderer
	 * along to it, so there's no need to call this method yourself.
	 */
	public abstract void init();

	/**
	 * Render to the SplashScreen window. Note that all {@code RenderElement}s
	 * should call their {@code transform()} method and pass {@code SplashScreen.splashWindow}
	 * to it to ensure proper scaling.
	 */
	public abstract void render();


	/**
	 * Clean up any objects necessary. If your SplashRenderer uses any
	 * {@code GUIText} objects, for example, those should be deleted here.
	 * This will be called automatically by the SplashRenderer when the
	 * splash screen window is destroyed, so there's no need to call this
	 * method yourself.
	 */
	public abstract void cleanUp();

}
