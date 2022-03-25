package com.floober.engine.core.renderEngine.display;


import com.floober.engine.core.assets.loaders.GameLoader;
import com.floober.engine.core.assets.loaders.ImageLoader;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.particles.ParticleMaster;
import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.renderEngine.textures.RawTextureData;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Config;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.math.MathUtil;
import com.floober.engine.core.util.time.TimeScale;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.GL43.glDebugMessageControl;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

	public static GLFWWindowSizeCallback windowResizeCallback;
	public static GLFWWindowCloseCallback windowCloseCallback;

	// Global settings
	public static int FPS_CAP = 144;

	// window icons
	private static GLFWImage.Buffer iconBuffer;

	// timings
	private static long lastFrameTime;
	private static long currentFrameDelta;
	private static float delta;
	private static long gameStartTime;

	// All displays
	private static final List<Window> windows = new ArrayList<>();
	private static final HashMap<Long, Window> windowsByID = new HashMap<>();
	public static long primaryWindowID;

	// Fetching windows
	public static List<Window> getWindows() {
		return windows;
	}

	public static HashMap<Long, Window> getWindowsByID() {
		return windowsByID;
	}

	public static Window getWindow(long windowID) {
		return windowsByID.get(windowID);
	}

	private static void addWindow(Window window) {
		windows.add(window);
		windowsByID.put(window.getWindowID(), window);
		KeyInput.windowKeyboardAdapters.put(window.getWindowID(), new KeyInput());
		MouseInput.windowMouseAdapters.put(window.getWindowID(), new MouseInput());
	}

	// INITIALIZING
	private static void initWindowIcons() {
		// allocate memory
		GLFWImage icon64 = GLFWImage.malloc();
		GLFWImage icon48 = GLFWImage.malloc();
		GLFWImage icon32 = GLFWImage.malloc();
		// allocate buffer
		iconBuffer = GLFWImage.malloc(3);
		// load the primary and secondary icons
		RawTextureData imageData1 = ImageLoader.loadImageRaw(Config.ICON_PATH_64);
		RawTextureData imageData2 = ImageLoader.loadImageRaw(Config.ICON_PATH_48);
		RawTextureData imageData3 = ImageLoader.loadImageRaw(Config.ICON_PATH_32);
		icon64.set(imageData1.width, imageData1.height, imageData1.buffer);
		icon48.set(imageData2.width, imageData2.height, imageData2.buffer);
		icon32.set(imageData3.width, imageData3.height, imageData3.buffer);
		// put the data in the buffer
		iconBuffer.put(0, icon64);
		iconBuffer.put(1, icon48);
		iconBuffer.put(2, icon32);
	}

	private static void initResizeCallback() {
		windowResizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long windowID, int width, int height) {
				// get the window target
				Window targetWindow = windowsByID.get(windowID);
				// log it
				float scaleToFitWidth = (float) width / Config.INTERNAL_WIDTH;
				float scaleToFitHeight = (float) height / Config.INTERNAL_HEIGHT;
				targetWindow.setWidth(width);
				targetWindow.setHeight(height);
				if (scaleToFitWidth < scaleToFitHeight) {
					// scale to fit width
					float aspectRatio = (float) Config.INTERNAL_HEIGHT / Config.INTERNAL_WIDTH;
					int bottomY = (int) ((height - width * aspectRatio) / 2);
					targetWindow.setViewport(0, bottomY, width, (int) (width * aspectRatio));
					targetWindow.updateRatio(width, width * aspectRatio);
					targetWindow.setMouseOffset(0, -bottomY);
					Logger.log(	"Resizing to fit width! Window resized to [" + width + " x " + height + "]; " +
								"Viewport is now [" + width + " x " + (int) (width * aspectRatio) + "], " +
								"Mouse Offset is now (0, " + (-bottomY / 2) + "); Scale used was width: " + scaleToFitWidth);
				} else {
					// scale to fit height
					float aspectRatio = (float) Config.INTERNAL_WIDTH / Config.INTERNAL_HEIGHT;
					int leftX = (int) ((width - height * aspectRatio) / 2);
					targetWindow.setViewport(leftX, 0, (int) (height * aspectRatio), height);
					targetWindow.updateRatio(height * aspectRatio, height);
					targetWindow.setMouseOffset(-leftX, 0);
					Logger.log(	"Resizing to fit height! Window resized to [" + width + " x " + height + "]; " +
								"Viewport is now [" + (int) (height * aspectRatio) + " x " + height + "], " +
								"Mouse Offset is now (" + -leftX + ", 0); Scale used was height: " + scaleToFitHeight);
				}

				// TEST
				// poll input
				KeyInput.update();
				MouseInput.update();

				// time
				TimeScale.update();

				// this is the best condition I could come up with that prevents
				// these processes from executing before the necessary data/assets
				// are loaded/created
				if (GameLoader.LOAD_COMPLETE) {
					// run game logic
					Game.update();

					if (targetWindow.isReady()) {

						// Set target
						targetWindow.getWindowRenderer().prepare();

						// update inputs for this window
						KeyInput.update();
						MouseInput.update();

						// render game internally
						Game.render();
						Render.fillScreen(Colors.GRAY);
						if (targetWindow.getWindowID() == DisplayManager.primaryWindowID) Render.drawRect(Colors.RED, 1920/2f, 1080/2f, 0, 300, 300, true);
						Render.drawRect(Colors.GREEN, MouseInput.getX(), MouseInput.getY(), 0, 80, 80, true);

						// render to the screen
						MasterRenderer.currentRenderTarget.render();

					}

				} // this whole section is indented an extra level because it's actually inside a collapsed invoke() method

				// update display and poll events
				DisplayManager.updateDisplay();
			}
		};
	}

	private static void initCloseCallback() {
		windowCloseCallback = new GLFWWindowCloseCallback() {
			@Override
			public void invoke(long windowID) {
				windowsByID.get(windowID).setShouldClose(true);
				Logger.log("Window " + windowID + " set to close!");
			}
		};
	}

	public static Window getPrimaryGameWindow() {
		return windowsByID.get(primaryWindowID);
	}

	public static void initPrimaryGameWindow() {
		// Pre-startup: Set up error printing.
		GLFWErrorCallback.createPrint(System.err).set();

		// Step 1: init GLFW. If this fails, the game cannot run, so die.
		if (!glfwInit()) {
			throw new IllegalStateException("GLFW failed to initialize.");
		}

		// Step 2: Set the window hints (settings).
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRANSPARENT_FRAMEBUFFER);
		glfwWindowHint(GLFW_AUTO_ICONIFY, GL_FALSE);

		// Step 3: Create the window. The process depends on whether we're creating a fullscreen window or not.
		if (Config.FULLSCREEN) {
			GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			assert videoMode != null;
			glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
			glfwWindowHint(GLFW_RED_BITS, videoMode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, videoMode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, videoMode.blueBits());
			glfwWindowHint(GLFW_REFRESH_RATE, videoMode.refreshRate());
			primaryWindowID = glfwCreateWindow(videoMode.width(), videoMode.height(), Config.WINDOW_TITLE, glfwGetPrimaryMonitor(), NULL);
			glfwSetInputMode(primaryWindowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		} else {
			primaryWindowID = glfwCreateWindow(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, Config.WINDOW_TITLE, NULL, NULL);
		}

		// Step 3.5: Test to make sure the window creation succeeded. If it fails, die.
		if (primaryWindowID == NULL) {
			throw new IllegalStateException("Unable to create GLFW window.");
		}

		// Step 4: Set up callbacks
		glfwSetKeyCallback(primaryWindowID, (window, key, scancode, action, mods) -> {
		});
		if (glfwRawMouseMotionSupported())
			glfwSetInputMode(primaryWindowID, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
		glfwSetScrollCallback(primaryWindowID, (theWindowID, xoffset, yoffset) -> MouseInput.windowMouseAdapters.get(primaryWindowID).WHEEL = yoffset);

		// Step 5: Load and set the window icon.
		initWindowIcons();
		// set the icons
		glfwSetWindowIcon(primaryWindowID, iconBuffer);

		// Step 6: Size and position the window.
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(primaryWindowID, pWidth, pHeight);

			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			assert vidMode != null; // compiler won't shut up about this
			glfwSetWindowPos(primaryWindowID, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);

			glfwMakeContextCurrent(primaryWindowID);
			glfwSwapInterval(0);
		}

		// Step 7: Tell GLFW what to do when a window is resized or closed.
		initResizeCallback();
		initCloseCallback();

		// The primary window only reacts to resize events; the close callback is for auxiliary windows.
		glfwSetWindowSizeCallback(primaryWindowID, windowResizeCallback);

		// Step 8: Init OpenGL.
		GL.createCapabilities();

		glDepthFunc(GL_LEQUAL);
		glDepthRange(0, 1000000);

		glClearColor(0, 0, 0, 1);
		glClearDepth(1);

		GLUtil.setupDebugMessageCallback();
		glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);

		// CREATE THE WINDOW OBJECT AND STORE IT
		Window mainGameWindow = new Window(primaryWindowID, null, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT, 0, 0, 0, 0);
		addWindow(mainGameWindow);

		mainGameWindow.setWindowRenderer(new MasterRenderer(mainGameWindow));

		// Init master renderer to allow the window creation process to finish
//		MasterRenderer.init(primaryWindowID);
//		mainGameWindow.setWindowRenderer(MasterRenderer.primaryWindowRenderer);

		// Step 9: Manually resize to size specified in Config.
		glfwSetWindowSize(primaryWindowID, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT);
		windowResizeCallback.invoke(primaryWindowID, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT);
		centerWindow(primaryWindowID);

		mainGameWindow.setReady(true);

		// Step 9.5: Fill the window with the starting load color
//		glClearColor(1, 1, 1, 1);
//		glClear(GL_COLOR_BUFFER_BIT);

		// Step 10: Make the window visible!
		glfwShowWindow(primaryWindowID);

		// Step 11: Start the game time
		DisplayManager.start();

		// Done!
	}

	/**
	 * Create a new window with the specified dimensions and move it to the
	 * specified pixel position on the player's display.
	 *
	 * @param width  the desired width of the window in pixels
	 * @param height the desired height of the window in pixels
	 * @param x      the desired pixel x-coordinate of the window
	 * @param y      the desired pixel y-coordinate of the window
	 * @return a new Window object handle for the created window
	 */
	public static Window createNewWindow(int width, int height, int x, int y) {
		// create the window with GLFW
		long windowID = glfwCreateWindow(width, height, "", NULL, NULL);
		// confirm that the window was created, or log an error otherwise
		if (windowID == NULL) {
			throw new IllegalStateException("Unable to create GLFW window.");
		}
		// Create the window object and store it
		Window window = new Window(windowID, null, width, height, 0, 0, 0, 0);
		addWindow(window);
		window.setWindowRenderer(new MasterRenderer(window));
		window.setWindowTitle(Config.WINDOW_TITLE + " #" + (windows.size() + 1));
		// size the window as requested
		glfwSetWindowSize(windowID, width, height);
		windowResizeCallback.invoke(windowID, width, height);
		// position the window as requested
		glfwSetWindowPos(windowID, x, y);
		// configure the window settings
		glfwSetWindowSizeCallback(windowID, windowResizeCallback);
		glfwSetWindowCloseCallback(windowID, windowCloseCallback);
		glfwSetWindowIcon(windowID, iconBuffer);
		// configure window mouse input
		if (glfwRawMouseMotionSupported())
			glfwSetInputMode(windowID, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
		glfwSetScrollCallback(windowID, (theWindowID, xoffset, yoffset) -> MouseInput.windowMouseAdapters.get(windowID).WHEEL = yoffset);
		// make the window visible
		glfwShowWindow(windowID);
		// return the window handle object
		window.setReady(true);
		return window;
	}

	public static void checkToggleFullscreen() {
		if (KeyInput.isPressed(KeyInput.F11)) {
			Window.fullscreen = !Window.fullscreen;
			Logger.log("Attempting to toggle full screen");
			if (Window.fullscreen) {
				glfwSetWindowMonitor(primaryWindowID, glfwGetPrimaryMonitor(), 0, 0, Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, DisplayManager.FPS_CAP);
			} else {
				glfwSetWindowMonitor(primaryWindowID, MemoryUtil.NULL, 0, 0, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT, GLFW_DONT_CARE);
				centerWindow(primaryWindowID);
			}
		}
	}

	public static void updateDisplay() {
		for (int i = 0; i < windows.size(); i++) {
			long windowID = windows.get(i).getWindowID();
			if (windows.get(i).shouldClose()) {
				glfwDestroyWindow(windowID);
				windows.remove(i);
				KeyInput.windowKeyboardAdapters.remove(windowID);
				MouseInput.windowMouseAdapters.remove(windowID);
				i--;
			}
		}
		// update time values
		long currentFrameTime = getCurrentTime();
		currentFrameDelta = currentFrameTime - lastFrameTime;
		delta = currentFrameDelta / 1000f;
		lastFrameTime = currentFrameTime;
	}

	public static void centerWindow(long windowID) {
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		if (vidMode != null)
			glfwSetWindowPos(windowID, (vidMode.width() - Config.DEFAULT_WIDTH) / 2, (vidMode.height() - Config.DEFAULT_HEIGHT) / 2);
		else
			Logger.logError("COULD NOT CENTER WINDOW: glfwGetVideoMode() returned NULL");
	}

	public static void start() {
		gameStartTime = System.nanoTime();
	}

	// CLEAN UP METHODS

	/**
	 * Clean up GLFW by freeing all Callbacks and destroying all windows.
	 */
	public static void cleanUp() {
		freeCallbacks();
		destroyWindows();
	}

	private static void freeCallbacks() {
		Callbacks.glfwFreeCallbacks(primaryWindowID);
		for (Long windowID : windowsByID.keySet()) {
			if (windowID != primaryWindowID) Callbacks.glfwFreeCallbacks(windowID);
		}
	}

	private static void destroyWindows() {
		glfwDestroyWindow(primaryWindowID);
		for (Long windowID : windowsByID.keySet()) {
			if (windowID != primaryWindowID) glfwDestroyWindow(windowID);
		}
	}

	// TIME METHODS

	/**
	 * Get the time since the last frame completion in seconds.
	 *
	 * @return The frame time, scaled by the current value in {@code TimeScale}.
	 */
	public static float getFrameTimeSeconds() {
		return delta * TimeScale.getTimeScale();
	}

	public static float getFrameTimeRaw() {
		return delta;
	}

	// frame time in nanoseconds
	public static float getCurrentFrameDelta() {
		return currentFrameDelta * TimeScale.getTimeScale();
	}

	public static long getCurrentFrameDeltaLong() {
		return (long) (currentFrameDelta * TimeScale.getTimeScale());
	}

	public static long getCurrentFrameDeltaRaw() {
		return currentFrameDelta;
	}

	public static float getGameTime() {
		return (System.nanoTime() - gameStartTime) / 1_000_000f / 1000f;
	}

	public static long getGameTimeMS() {
		return (System.nanoTime() - gameStartTime) / 1_000_000;
	}

	// used as timer
	public static long getCurrentTime() {
		return (long) (glfwGetTime() * 1000);
	}

	/**
	 * Convert the position and scale values given from pixel units to screen units,
	 * and return the results in a Vector3f that can be passed to a vertex shader.
	 *
	 * @param x        The x coordinate
	 * @param y        The y coordinate
	 * @param z        The z coordinate
	 * @param width    The width of the element
	 * @param height   The height of the element
	 * @param centered Whether to use the width and height values to center the new position on the given (x,y) position
	 * @return A Vector3f representing the given pixel coordinates in screen coordinates.
	 */
	public static Vector3f convertToDisplayPosition(float x, float y, float z, float width, float height, boolean centered) {
		return convertToDisplayPosition(MasterRenderer.getTargetWindowID(), x, y, z, width, height, centered);
	}

	/**
	 * Convert the position and scale values given from pixel units to screen units,
	 * and return the results in a Vector3f that can be passed to a vertex shader.
	 *
	 * @param window   the ID of the window to scale for
	 * @param x        The x coordinate
	 * @param y        The y coordinate
	 * @param z        The z coordinate
	 * @param width    The width of the element
	 * @param height   The height of the element
	 * @param centered Whether to use the width and height values to center the new position on the given (x,y) position
	 * @return A Vector3f representing the given pixel coordinates in screen coordinates.
	 */
	public static Vector3f convertToDisplayPosition(long window, float x, float y, float z, float width, float height, boolean centered) {
		Window targetWindow = windowsByID.get(window);
		// translate to top left
		if (!centered) {
			x += width / 2;
			y += height / 2;
		}
		// invert y axis
		y = Config.INTERNAL_HEIGHT - y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Config.INTERNAL_WIDTH) * x;
		float displayY = -1 + (2f / Config.INTERNAL_HEIGHT) * y;
//		Logger.log("Scale was calculated using screen dimensions [" + Config.INTERNAL_WIDTH + " x " + Config.INTERNAL_HEIGHT + "]");
		// convert Z position to [0 ... 1]
		float displayZ = 1 - MathUtil.interpolateBounded(0, Layers.TOP_LAYER, z);
//		Logger.log("Layer " + z + " converted to z-position " + displayZ);
		// return the result
		return new Vector3f(displayX, displayY, displayZ);
	}

	public static Vector2f convertToDisplayPosition2D(Vector2f position) {
		return convertToDisplayPosition2D(MasterRenderer.getTargetWindowID(), position);
	}

	public static Vector2f convertToDisplayPosition2D(long window, Vector2f position) {
		Window targetWindow = windowsByID.get(window);
		// invert y axis
		float y = Config.INTERNAL_HEIGHT - position.y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Config.INTERNAL_WIDTH) * position.x;
		float displayY = -1 + (2f / Config.INTERNAL_HEIGHT) * y;
		// return the result
		return new Vector2f(displayX, displayY);
	}

	public static Vector2f convertToDisplayPosition2DNoInvertY(Vector2f position) {
		return convertToDisplayPosition2DNoInvertY(MasterRenderer.getTargetWindowID(), position);
	}

	public static Vector2f convertToDisplayPosition2DNoInvertY(long window, Vector2f position) {
		Window targetWindow = windowsByID.get(window);
		// invert y axis
//		float y = Config.INTERNAL_HEIGHT - position.y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Config.INTERNAL_WIDTH) * position.x;
		float displayY = -1 + (2f / Config.INTERNAL_HEIGHT) * position.y; // used to use commented-out y variable above ^^^
		// return the result
		return new Vector2f(displayX, displayY);
	}

	public static Vector2f convertToScreenPos(Vector2f position) {
		return convertToScreenPos(MasterRenderer.getTargetWindowID(), position);
	}

	public static Vector2f convertToScreenPos(long window, Vector2f position) {
		Window targetWindow = windowsByID.get(window);
//		Logger.log("Position " + position);
		// invert y axis
		float y = Config.INTERNAL_HEIGHT - position.y;
		// convert pixel coordinates to OpenGL coordinates
		float displayX = -1 + (2f / Config.INTERNAL_WIDTH) * position.x;
		float displayY = -1 + (2f / Config.INTERNAL_HEIGHT) * y;
		// return the result
//		Logger.log("Converted to position " + new Vector2f(displayX, displayY));
		return new Vector2f(displayX, displayY);
	}

	public static Vector2f convertToTextScreenPos(Vector2f position) {
//		Logger.log("Position " + position);
		// convert pixel coordinates to OpenGL coordinates
		float displayX = (1f / Config.INTERNAL_WIDTH) * position.x;
		float displayY = (1f / Config.INTERNAL_HEIGHT) * position.y;
		// return the result
//		Logger.log("Converted to position " + new Vector2f(displayX, displayY));
		return new Vector2f(displayX, displayY);
	}

	/**
	 * Convert the dimensions given from pixel units to screen units, and
	 * return the result in a Vector2f that can be passed to a vertex shader.
	 *
	 * @param width  The width of the element
	 * @param height The height of the element
	 * @return A Vector2f representing the dimensions of the element on the screen.
	 */
	public static Vector2f convertToDisplayScale(float width, float height) {
		return convertToDisplayScale(MasterRenderer.getTargetWindowID(), width, height);
	}

	/**
	 * Convert the dimensions given from pixel units to screen units, and
	 * return the result in a Vector2f that can be passed to a vertex shader.
	 *
	 * @param window the window to scale for
	 * @param width  The width of the element
	 * @param height The height of the element
	 * @return A Vector2f representing the dimensions of the element on the screen.
	 */
	public static Vector2f convertToDisplayScale(long window, float width, float height) {
		Window targetWindow = windowsByID.get(window);
		float displayWidth = width / Config.INTERNAL_WIDTH;
		float displayHeight = height / Config.INTERNAL_HEIGHT;
		return new Vector2f(displayWidth, displayHeight);
	}

	public static float convertToScreenSize(long window, float pixels) {
		return pixels / windowsByID.get(window).getWidth();
	}

}