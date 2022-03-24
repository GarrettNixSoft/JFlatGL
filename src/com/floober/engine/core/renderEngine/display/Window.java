package com.floober.engine.core.renderEngine.display;

import com.floober.engine.core.renderEngine.renderers.MasterRenderer;
import com.floober.engine.core.util.configuration.Config;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Window {

	// rendering
	private MasterRenderer windowRenderer;

	// internal resolution
	private int width, height;
	private final Vector2f screenRatio = new Vector2f();
	public static boolean fullscreen = false;

	private int xOffset;
	private int yOffset;
	private int viewportWidth;
	private int viewportHeight;

	private int mouseXOffset;
	private int mouseYOffset;
	private double mouseXRatio;
	private double mouseYRatio;

	private final long windowID;
	private String windowTitle = Config.WINDOW_TITLE;

	private boolean ready = false;
	private boolean shouldClose = false;

	public Window(long windowID, MasterRenderer windowRenderer, int width, int height, int xOffset, int yOffset, int viewportWidth, int viewportHeight) {
		this.windowID = windowID;
		this.windowRenderer = windowRenderer;
		this.width = width;
		this.height = height;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
	}

//	public Window(long windowID, int width, int height, int xOffset, int yOffset, int viewportWidth, int viewportHeight) {
//		this.windowID = windowID;
//		this.width = width;
//		this.height = height;
//		this.xOffset = xOffset;
//		this.yOffset = yOffset;
//		this.viewportWidth = viewportWidth;
//		this.viewportHeight = viewportHeight;
//		// create a new renderer
//		windowRenderer = new MasterRenderer(this);
//	}

	public void makeContextCurrent() {
		glfwMakeContextCurrent(windowID);
	}

	public void updateRatio(double width, double height) {
		mouseXRatio = Config.INTERNAL_WIDTH / width;
		mouseYRatio = Config.INTERNAL_HEIGHT / height;
		screenRatio.set(mouseXRatio, mouseYRatio);
	}

	public void swapBuffers() {
		glfwSwapBuffers(windowID);
		glfwPollEvents();
	}

	public void update() {
		// TODO: window shake effect
	}

	// GETTERS
	public long getWindowID() {
		return windowID;
	}

	public String getWindowTitle() { return windowTitle; }

	public MasterRenderer getWindowRenderer() {
		return windowRenderer;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean shouldClose() {
		return shouldClose;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public int getMouseXOffset() {
		return mouseXOffset;
	}

	public int getMouseYOffset() {
		return mouseYOffset;
	}

	public Vector2f getScreenRatio() {
		return screenRatio;
	}

	public double getMouseXRatio() {
		return mouseXRatio;
	}

	public double getMouseYRatio() {
		return mouseYRatio;
	}

	public int getViewportWidth() {
		return viewportWidth;
	}

	public int getViewportHeight() {
		return viewportHeight;
	}

	// SETTERS
	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		glfwSetWindowTitle(windowID, windowTitle);
	}

	public void setWindowRenderer(MasterRenderer windowRenderer) {
		this.windowRenderer = windowRenderer;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setShouldClose(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public void setMouseOffset(int x, int y) {
		setMouseXOffset(x);
		setMouseYOffset(y);
	}

	public void setMouseXOffset(int mouseXOffset) {
		this.mouseXOffset = mouseXOffset;
	}

	public void setMouseYOffset(int mouseYOffset) {
		this.mouseYOffset = mouseYOffset;
	}

	public void setViewportWidth(int viewportWidth) {
		this.viewportWidth = viewportWidth;
	}

	public void setViewportHeight(int viewportHeight) {
		this.viewportHeight = viewportHeight;
	}

	public void setViewport(int x, int y, int width, int height) {
		makeContextCurrent();
		xOffset = x;
		yOffset = y;
		viewportWidth = width;
		viewportHeight = height;
		glViewport(x, y, width, height);
	}

	/**
	 * Reset the display to the viewport described by the current
	 * values for {@code X_OFFSET}, {@code Y_OFFSET}, {@code WIDTH}, and {@code HEIGHT}.
	 */
	public void setViewport() {
		makeContextCurrent();
		glViewport(xOffset, yOffset, viewportWidth, viewportHeight);
	}

	public static float mainCenterX() { return DisplayManager.getPrimaryGameWindow().centerX(); }

	public static float mainCenterY() { return DisplayManager.getPrimaryGameWindow().centerY(); }

	public float centerX() {
		return Config.INTERNAL_WIDTH / 2f;
	}

	public float centerY() {
		return Config.INTERNAL_HEIGHT / 2f;
	}

	public Vector2f center() {
		return new Vector2f(centerX(), centerY());
	}

	public Vector2f dimensions() {
		return new Vector2f(Config.INTERNAL_WIDTH, Config.INTERNAL_HEIGHT);
	}

}