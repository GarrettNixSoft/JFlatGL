package com.floober.engine.core.splash;

import com.floober.engine.core.assets.TextureAnalyzer;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.configuration.Config;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SplashScreen {

	private static MasterRenderer splashRenderer;

	private static Window splashWindow;
	private static long windowID;

	public static void init() {

		// create the window with GLFW
		windowID = glfwCreateWindow(Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT, "", NULL, NULL);
		// confirm that the window was created, or log an error otherwise
		if (windowID == NULL) {
			throw new IllegalStateException("Unable to create GLFW window.");
		}
		// do not decorate the window
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
		glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
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
		// set the clear color to black
		glClearColor(0, 0, 0, 1);
		// show the window
		glfwShowWindow(windowID);

		// make a renderer to use
		splashWindow = new Window(windowID, Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT, 0, 0, 0, 0);
		DisplayManager.addWindow(splashWindow);
		splashRenderer = splashWindow.getWindowRenderer();
		splashRenderer.generateUnregisteredSceneBuffer(splashWindow);
		TextMaster.init();
		TextureAnalyzer.init();

		// I think that's all?
	}

	public static void render() {
		glfwMakeContextCurrent(windowID);
		glClear(GL_COLOR_BUFFER_BIT);
		splashRenderer.prepare(true);
		splashRenderer.render(true);
		splashWindow.swapBuffers();
	}

	public static void close() {
		glfwDestroyWindow(windowID);
	}

}
