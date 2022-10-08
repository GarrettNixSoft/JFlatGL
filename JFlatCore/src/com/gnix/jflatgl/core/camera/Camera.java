package com.gnix.jflatgl.core.camera;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.entity.effects.Shake2D;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.interpolators.TransitionFloat;
import com.gnix.jflatgl.core.util.time.Timer;
import org.joml.Vector2f;

public class Camera {

	// ******************************** SINGLETON INSTANCE ********************************
	private static Camera instance;

	// camera controls world offsets and scaling (zoom)
	private float xOffset, yOffset;
	private float scale = 1;
	private final float minScale = 0.1f, maxScale = 3f;

	private TransitionFloat zoomInterpolator;

	// world boundaries
	private int worldWidth, worldHeight;
	private int xmin, xmax;
	private int ymin, ymax;

	// viewport
	private int viewportWidth, viewportHeight;

	// movement mode(s)
	private int currentMode;
	public static final int INIT = -1, FOLLOW = 0, TARGET = 1, FREE = 2;

	// ******************************** EFFECTS ********************************
	// SHAKE
	private final Shake2D shakeEffect;
	private Timer shakeTimer;
	private boolean shake;

	// ******************************** CONSTRUCTOR ********************************
	private Camera() {
		// default follow strength
		currentMode = INIT;
		// create effects
		shakeEffect = new Shake2D();
		zoomInterpolator = new TransitionFloat();
	}

	// ******************************** GETTING THE INSTANCE ********************************
	public static Camera getInstance() {
		if (instance == null)
			instance = new Camera();
		return instance;
	}

	// ******************************** POSITION CONVERSIONS ********************************
	public float worldXToScreenX(float worldX) {
		return (worldX - xOffset) * scale;
	}

	public float worldYToScreenY(float worldY) {
		return (worldY - yOffset) * scale;
	}

	public float screenXToWorldX(float screenX) {
		return screenX / scale + xOffset;
	}

	public float screenYToWorldY(float screenY) {
		return screenY / scale + yOffset;
	}

	// ******************************** GETTERS ********************************
	public float getOffsetX() {
		return xOffset + shakeEffect.getX();
	}

	public float getOffsetY() {
		return yOffset + shakeEffect.getY();
	}

	public float getScale() {
		return scale;
	}

	public Vector2f getPositionVec() {
		return getFinalPosition();
	}

	public Vector2f getRawPos() {
		return new Vector2f(xOffset, yOffset);
	}

	public int getCurrentMode() {
		return currentMode;
	}

	// ******************************** SETTERS ********************************
	public void setScale(float newScale) {
		if (newScale >= minScale && newScale <= maxScale) {
			scale = newScale;
		}
	}

	public void setPosition(Vector2f position) {
		// TODO
	}

	// ******************************** MOVING ********************************
	public void move(float x, float y) {
		xOffset += x;
		yOffset += y;
		fixBounds();
	}

	public void moveX(float x) {
		xOffset += x;
		fixBounds();
	}

	public void moveY(float y) {
		yOffset += y;
		fixBounds();
	}

	// ******************************** SCALING/ZOOMING ********************************
	public void zoom(float targetScale, float time) {
		if (targetScale < minScale || targetScale > maxScale) return;
		zoomInterpolator.transitionTo(scale, targetScale, time);
	}

	public void zoomIn(float amount, float time) {
		float targetScale = scale + amount;
		if (targetScale < minScale || targetScale > maxScale) return;
		zoomInterpolator.transitionTo(scale, targetScale, time);
	}

	public void zoomOut(float amount, float time) {
		float targetScale = scale - amount;
		if (targetScale < minScale || targetScale > maxScale) return;
		zoomInterpolator.transitionTo(scale, targetScale, time);
	}

	private void doZoom() {
		if (zoomInterpolator.isRunning()) {
			// update the interpolator to get the next scale value
			zoomInterpolator.update();
			// save the current center position
			float centerXBeforeScale = screenXToWorldX(Game.centerX());
			float centerYBeforeScale = screenYToWorldY(Game.centerY());
			// apply the new scale
			float oldScale = scale;
			scale = zoomInterpolator.getValue();
			setDimensions();
			// adjust the offset to keep the center in the same spot
			float centerXAfterScale = screenXToWorldX(Game.centerX());
			float centerYAfterScale = screenYToWorldY(Game.centerY());
			xOffset += (centerXBeforeScale - centerXAfterScale);
			yOffset += (centerYBeforeScale - centerYAfterScale);
			fixBounds();
			// log call
			Logger.log("Scale: " + scale + " (Delta: " + (scale - oldScale) + ")");
			Logger.log("Offsets: %.2f, %.2f", xOffset, yOffset);
		}
	}

	// ******************************** INTERNAL METHODS ********************************
	public void setWorldSize(int worldWidth, int worldHeight) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		setDimensions();
	}

	public void setViewportSize(int viewportWidth, int viewportHeight) {
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		setDimensions();
	}

	private void setDimensions() {

		xmin = 0;
		xmax = (int) ((worldWidth * scale - viewportWidth) / scale);
//		xmax = (int) (worldWidth / scale - viewportWidth);
		ymin = 0;
		ymax = (int) ((worldHeight * scale - viewportHeight) / scale);
//		ymax = (int) (worldHeight / scale - viewportHeight);

		Logger.log("Max offsets: " + xmax + ", " + ymax);

	}

	private void fixBounds() {

		// special cases: screen space exceeds world size at current scale
		if (xmax < xmin) {
			float diff = xmax - xmin;
			xOffset = diff / 2;
		}
		else { // general cases
			if (xOffset < xmin) xOffset = xmin;
			if (xOffset > xmax) xOffset = xmax;
		}
		if (ymax < ymin) {
			float diff = ymax - ymin;
			yOffset = diff / 2;
		}
		else { // general cases
			if (yOffset < ymin) yOffset = ymin;
			if (yOffset > ymax) yOffset = ymax;
		}
	}

	// ******************************** ACTIONS ********************************
	public void shake(float time, int xSeverity, int ySeverity) {
		shake = true;
		shakeEffect.setSeverity(xSeverity, ySeverity);
		shakeEffect.activate();
		shakeTimer = new Timer(time);
		shakeTimer.start();
	}

	// shake indefinitely
	public void shakeToggle(int xSeverity, int ySeverity) {
		if (shake) {
			shake = false;
		}
		else {
			shake = true;
			shakeEffect.setSeverity(xSeverity, ySeverity);
			shakeEffect.activate();
			shakeTimer = new Timer(Float.MAX_VALUE); // 3.40282347 x 10^38 seconds, or 1.07831272 × 10^31 years
			shakeTimer.start();
		}
	}

	// ******************************** UPDATE METHODS ********************************
	private void doMovement() {
		switch (currentMode) {
			case FOLLOW -> {
				// TODO
			}
			case TARGET -> {
				// TODO
			}
			case FREE -> {} // don't move
		}
	}

	private Vector2f getPositionVector() {
		float x = Game.centerX() - xOffset;
		float y = Game.centerY() - yOffset;
		return new Vector2f(x, y);
	}

	private Vector2f getFinalPosition() {
		Vector2f position;
		Vector2f rawPosition = getPositionVector();
		if (shake)
			position = rawPosition.add(shakeEffect.getOffset());
		else position = rawPosition;
		// return final vector
		return position;
	}

	private void doEffects() {
		if (shake) {
			shakeEffect.update();
			if (shakeTimer.finished()) {
				shakeEffect.deactivate();
				shake = false;
			}
		}
	}

	// ******************************** MASTER UPDATE ********************************
	public void update() {
		// update camera position
		doMovement();
		fixBounds();
		// update zoom
		doZoom();
		// update effects
		doEffects();
		// set position
		Vector2f position = getFinalPosition();
	}

}
