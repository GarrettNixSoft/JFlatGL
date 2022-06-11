package com.floober.engine.core.splash;

import com.floober.engine.core.assets.TextureAnalyzer;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.time.Timer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SplashScreen {

	private static MasterRenderer splashRenderer;

	public static boolean SPLASH_RENDER = false;

	public static Window splashWindow;
	public static long windowID;

	private static final Timer timer = new Timer(-1);

	public static void init() {
		// do not decorate the window
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
		glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
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
		glClearColor(0, 0, 0, 1);
		glClearDepth(1);
		// show the window
		glfwShowWindow(windowID);

		// make a renderer to use
		splashWindow = new Window(windowID, Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT, 0, 0, 0, 0);
		DisplayManager.addWindow(splashWindow);
		splashRenderer = splashWindow.getWindowRenderer();
		splashRenderer.generateUnregisteredSceneBuffer(splashWindow);
		TextMaster.init();
		TextureAnalyzer.init();

		glViewport(0, 0, Config.SPLASH_WIDTH, Config.SPLASH_HEIGHT);

		// I think that's all?
	}

	public static void render() {
		if (!timer.started()) timer.start();

		SPLASH_RENDER = true;
		splashRenderer.prepare(false);
//		glfwMakeContextCurrent(windowID);

		float size = Config.SPLASH_WIDTH * timer.getTimeElapsedSeconds() / 5;
		RectElement redRect = new RectElement(Colors.RED, 0, Config.SPLASH_HEIGHT - 50, Layers.BOTTOM_LAYER + 1, size, 50, false);
		redRect.transform(splashWindow);
		Render.drawRect(redRect);

		splashRenderer.render(false);
		splashWindow.swapBuffers();
	}

	public static void close() {
		SPLASH_RENDER = false;
		glfwDestroyWindow(windowID);
	}

}