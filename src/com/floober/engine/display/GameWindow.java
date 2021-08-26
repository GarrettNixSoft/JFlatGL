package com.floober.engine.display;

import com.floober.engine.loaders.ImageLoader;
import com.floober.engine.renderEngine.textures.RawTextureData;
import com.floober.engine.util.Logger;
import com.floober.engine.util.configuration.Config;
import com.floober.engine.util.input.MouseInput;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_SEVERITY_NOTIFICATION;
import static org.lwjgl.opengl.GL43.glDebugMessageControl;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow {

	public static long windowID;

	public static GLFWWindowSizeCallback windowResizeCallback;

	public static void initGame() {
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
			glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
			glfwWindowHint(GLFW_RED_BITS, videoMode.redBits());
			glfwWindowHint(GLFW_GREEN_BITS, videoMode.greenBits());
			glfwWindowHint(GLFW_BLUE_BITS, videoMode.blueBits());
			glfwWindowHint(GLFW_REFRESH_RATE, videoMode.refreshRate());
			windowID = glfwCreateWindow(videoMode.width(), videoMode.height(), Config.WINDOW_TITLE, glfwGetPrimaryMonitor(), NULL);
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}
		else {
			windowID = glfwCreateWindow(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT, Config.WINDOW_TITLE, NULL, NULL);
		}

		// Step 3.5: Test to make sure the window creation succeeded. If it fails, die.
		if (windowID == NULL) {
			throw new IllegalStateException("Unable to create GLFW window.");
		}

		// Step 4: Set up callbacks
		glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {});
		if (glfwRawMouseMotionSupported())
			glfwSetInputMode(windowID, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE);
		glfwSetScrollCallback(windowID, (windowID, xoffset, yoffset) -> MouseInput.WHEEL = yoffset);

		// Step 5: Load and set the window icon.
		// allocate memory
		GLFWImage icon64 = GLFWImage.malloc();
		GLFWImage icon48 = GLFWImage.malloc();
		GLFWImage icon32 = GLFWImage.malloc();
		// allocate buffer
		GLFWImage.Buffer iconBuffer = GLFWImage.malloc(3);
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
		// set the icons
		glfwSetWindowIcon(windowID, iconBuffer);

		// Step 6: Size and position the window.
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(windowID, pWidth, pHeight);

			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			assert vidMode != null; // compiler won't shut up about this
			glfwSetWindowPos(windowID, (vidMode.width() - pWidth.get(0)) / 2, (vidMode.height() - pHeight.get(0)) / 2);

			glfwMakeContextCurrent(windowID);
			glfwSwapInterval(0);
		}

		// Step 7: Tell GLFW what to do when the window is resized.
		windowResizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
//				// log it
				float scaleToFitWidth = (float) width / Config.INTERNAL_WIDTH;
				float scaleToFitHeight = (float) height / Config.INTERNAL_HEIGHT;
				Display.WINDOW_WIDTH = width;
				Display.WINDOW_HEIGHT = height;
				if (scaleToFitWidth < scaleToFitHeight) {
					// scale to fit width
					float aspectRatio = (float) Config.INTERNAL_HEIGHT / Config.INTERNAL_WIDTH;
//					float ratio16x9 = 9.0f / 16.0f;
					int topY = (int) ((height - width * aspectRatio) / 2);
					Display.setViewport(0, topY, width, (int) (width * aspectRatio));
//					glViewport(0, topY, width, (int) (width * ratio16x9));
					MouseInput.updateRatio(width, width * aspectRatio);
					MouseInput.setOffset(0, -topY);
					Logger.log("Resizing to fit width! Window resized to [" + width + " x " + height + "]; Viewport is now [" + width + " x " + (int) (width * aspectRatio) + "], Mouse Offset is now (0, " + (-topY / 2) + "); Scale used was width: " + scaleToFitWidth);
				}
				else {
					// scale to fit height
					float aspectRatio = (float) Config.INTERNAL_WIDTH / Config.INTERNAL_HEIGHT;
//					float ratio16x9 = 16.0f / 9.0f;
					int leftX = (int) ((width - height * aspectRatio) / 2);
					Display.setViewport(leftX, 0, (int) (height * aspectRatio), height);
//					glViewport(leftX, 0, (int) (height * ratio16x9), height);
					MouseInput.updateRatio(height * aspectRatio, height);
					MouseInput.setOffset(-leftX, 0);
					Logger.log("Resizing to fit height! Window resized to [" + width + " x " + height + "]; Viewport is now [" + (int) (height * aspectRatio) + " x " + height + "], Mouse Offset is now (" + -leftX + ", 0); Scale used was height: " + scaleToFitHeight);
				}
			}
		};

		glfwSetWindowSizeCallback(windowID, windowResizeCallback);

		// Step 8: Init OpenGL.
		GL.createCapabilities();

		glDepthFunc(GL_LEQUAL);
		glDepthRange(0, 1000000);

		glClearColor(0, 0, 0, 1);
		glClearDepth(1);

		GLUtil.setupDebugMessageCallback();
		glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);

		// Step 9: Manually resize to size specified in Config.
		glfwSetWindowSize(windowID, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT);
		windowResizeCallback.invoke(windowID, Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT);
		DisplayManager.centerWindow();

		// Step 9.5: Fill the window with the starting load color
//		glClearColor(1, 1, 1, 1);
//		glClear(GL_COLOR_BUFFER_BIT);

		// Step 10: Make the window visible!
		glfwShowWindow(windowID);

		// Step 11: Start the game time
		DisplayManager.start();

		// Done!
	}

}