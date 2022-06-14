package com.floober.engine.core.splash;

import com.floober.engine.core.assets.TextureAnalyzer;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.elements.RenderElement;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.time.Timer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SplashScreen {

	private static MasterRenderer renderer;
	private static SplashRenderer splashRenderer;

	public static boolean SPLASH_RENDER = false;

	public static Window splashWindow;
	public static long windowID;

	public static void init() {
		// do not decorate the window
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
		glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
		glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, Config.SPLASH_TRANSPARENT ? GLFW_TRUE : GLFW_FALSE);
		// create the window with GLFW
		windowID = glfwCreateWindow(Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT, "", NULL, NULL);
		// confirm that the window was created, or log an error otherwise
		if (windowID == NULL) {
			throw new IllegalStateException("Unable to create GLFW window.");
		}
		// center the window
		long primaryMonitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(primaryMonitor);
		int x = videoMode.width() / 2 - Config.SPLASH_WIDTH / 2;
		int y = videoMode.height() / 2 - Config.SPLASH_HEIGHT / 2;
		glfwSetWindowPos(windowID, x, y);
		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(0);
		// Step 8: Init OpenGL.
		GL.createCapabilities();
		// configure render settings
		glDepthFunc(GL_LEQUAL);
		glDepthRange(0, 1);
		glClearColor(0, 0, 0, Config.SPLASH_TRANSPARENT ? 0 : 1);
		glClearDepth(1);
		// show the window
		glfwShowWindow(windowID);

		// make a window handle and a renderer to use
		splashWindow = new Window(windowID, Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT, 0, 0, 0, 0);
		DisplayManager.addWindow(splashWindow);
		renderer = splashWindow.getWindowRenderer();
		renderer.generateUnregisteredSceneBuffer(splashWindow);

		// initialize the text master so text can be rendered on the splash screen
		TextMaster.init();

		// The texture analyzer only needs to be initialized once so do it before loading anything
		TextureAnalyzer.init();

		// Set the viewport so that everything gets properly rendered in the splash window
		glViewport(0, 0, Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT);

		// I think that's all?
	}

	/**
	 * Transform a RenderElement to scale properly on the
	 * Splash Screen window.
	 * @param renderElement the render element to transform
	 */
	public static void transform(RenderElement renderElement) {
		renderElement.transform(splashWindow);
	}

	/**
	 * Assign the SplashRenderer to use when drawing to the
	 * splash screen window.
	 * @param newSplashRenderer the renderer to use
	 */
	public static void prepare(SplashRenderer newSplashRenderer) {
		splashRenderer = newSplashRenderer;
		splashRenderer.init();
	}

	public static void render() {
		SPLASH_RENDER = true;
		renderer.prepare(false);

		splashRenderer.render();

		renderer.render(false);
		splashWindow.swapBuffers();

	}

	public static void close() {
		splashRenderer.cleanUp();
		renderer.cleanUpInstance();

		SPLASH_RENDER = false;
		glfwDestroyWindow(windowID);

		DisplayManager.removeWindow(splashWindow);
	}

}