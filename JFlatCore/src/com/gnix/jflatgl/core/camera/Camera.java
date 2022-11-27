package com.gnix.jflatgl.core.camera;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.entity.Entity;
import com.gnix.jflatgl.core.entity.effects.Shake2D;
import com.gnix.jflatgl.core.input.Cursor;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.interpolators.TransitionFloat;
import com.gnix.jflatgl.core.util.time.Timer;
import org.joml.Vector2f;

import java.util.Optional;

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

	// ******************************** MOVEMENT MODE(s) ********************************
	private int currentMode;
	public static final int INIT = -1, FOLLOW = 0, TARGET = 1, FREE = 2;

	private Optional<Entity> targetEntity = Optional.empty();
	private Optional<Vector2f> targetPoint = Optional.empty();

	private float followStrength = 0.07f; // TODO implement a setting system

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

	public Vector2f screenPosToWorldPos(Vector2f screenPos) {
		return new Vector2f(screenXToWorldX(screenPos.x), screenYToWorldY(screenPos.y));
	}

	public Vector2f worldPosToScreenPos(Vector2f worldPos) {
		return new Vector2f(worldXToScreenX(worldPos.x), worldYToScreenY(worldPos.y));
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

	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public int getViewportWidth() {
		return viewportWidth;
	}

	public int getViewportHeight() {
		return viewportHeight;
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

	public float worldMouseX() {
		return screenXToWorldX(Cursor.getX());
	}

	public float worldMouseY() {
		return screenYToWorldY(Cursor.getY());
	}

	public boolean notOnScreen(float x, float y, float width, float height, boolean centered) {

		float screenX = worldXToScreenX(x);
		float screenY = worldYToScreenY(y);
		float screenWidth = width * getScale();
		float screenHeight = height * getScale();

		if (!centered) {
			screenX += screenWidth / 2;
			screenY += screenHeight / 2;
		}

		return screenX + screenWidth / 2 < 0 ||
				screenX - screenWidth / 2 > viewportWidth ||
				screenY + screenHeight / 2 < 0 ||
				screenY - screenHeight / 2 > viewportHeight;

	}

	public boolean onScreen(float x, float y, float width, float height, boolean centered) {
		return !notOnScreen(x, y, width, height, centered);
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

	public void translate(float x, float y) {
		move(x, y);
	}

	// ******************************** TARGETING ********************************
	public void setFollowStrength(float followStrength) {
		this.followStrength = followStrength;
	}

	public void follow(Entity entity) {
		targetEntity = Optional.of(entity);
		currentMode = FOLLOW;
	}

	public void target(Vector2f point) {
		targetPoint = Optional.of(point);
		currentMode = TARGET;
	}

	public void freeCam() {
		currentMode = FREE;
	}

	public void snapToTarget() {
		if (currentMode == FOLLOW) {
			targetEntity.ifPresent(entity -> {
				// figure out where the center of the is in the world
				float x = screenXToWorldX(Game.centerX());
				float y = screenYToWorldY(Game.centerY());
				// figure out where the entity is relative to the visible center
				float xWalk = entity.getX() - x;
				float yWalk = entity.getY() - y;
				// figure out how far to move to cover the difference
				float dx = xWalk * 1;
				float dy = yWalk * 1;
				// move that distance
				xOffset += dx;
				yOffset += dy;
			});
		}
		else if (currentMode == TARGET) {
			targetPoint.ifPresent(point -> {
				// TODO
			});
		}
	}

	// ******************************** SCALING/ZOOMING ********************************
	public void zoom(float targetScale, float time) {
		if (targetScale < minScale || targetScale > maxScale) return;
		if (time == 0) {
			adjustForNewScale(targetScale);
		}
		else {
			zoomInterpolator.transitionTo(scale, targetScale, time);
		}
	}

	public void zoomIn(float amount, float time) {
		float targetScale = scale + amount;
		if (targetScale < minScale || targetScale > maxScale) return;
		if (time == 0) {
			adjustForNewScale(targetScale);
		}
		else {
			zoomInterpolator.transitionTo(scale, targetScale, time);
		}
	}

	public void zoomOut(float amount, float time) {
		float targetScale = scale - amount;
		if (targetScale < minScale || targetScale > maxScale) return;
		if (time == 0) {
			adjustForNewScale(targetScale);
		}
		else {
			zoomInterpolator.transitionTo(scale, targetScale, time);
		}
	}

	private void doZoom() {
		if (zoomInterpolator.isRunning()) {
			// update the interpolator to get the next scale value
			zoomInterpolator.update();
			// apply the new scale and adjust for it
			adjustForNewScale(zoomInterpolator.getValue());
		}
	}

	private void adjustForNewScale(float newScale) {
		// save the current center position
		float centerXBeforeScale = screenXToWorldX(Game.centerX());
		float centerYBeforeScale = screenYToWorldY(Game.centerY());
		// apply the new scale
		scale = newScale;
		setDimensions();
		// adjust the offset to keep the center in the same spot
		float centerXAfterScale = screenXToWorldX(Game.centerX());
		float centerYAfterScale = screenYToWorldY(Game.centerY());
		xOffset += (centerXBeforeScale - centerXAfterScale);
		yOffset += (centerYBeforeScale - centerYAfterScale);
		// keep everything in bounds
		fixBounds();
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
			case FOLLOW -> targetEntity.ifPresent(entity -> {
				// figure out how much to scale the ease factor, based on the current framerate relative to 60 FPS
				float frameRate = 1 / DisplayManager.getFrameTimeRaw();
				float timeRatio = 60.0f / frameRate;
				float ease = Math.max(followStrength * timeRatio, 0.0001f);
				// figure out where the center of the is in the world
				float x = screenXToWorldX(Game.centerX());
				float y = screenYToWorldY(Game.centerY());
				// figure out where the entity is relative to the visible center
				float xWalk = entity.getX() - x;
				float yWalk = entity.getY() - y;
				// figure out how far to move to cover the difference
				float dx = xWalk * ease;
				float dy = yWalk * ease;
				// move that distance
				xOffset += dx;
				yOffset += dy;
			});
			case TARGET -> {
				// TODO
			}
			case FREE -> {} // don't move on my own (something else is controlling the camera position)
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
