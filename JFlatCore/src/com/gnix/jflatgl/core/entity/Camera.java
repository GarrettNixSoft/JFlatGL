package com.gnix.jflatgl.core.entity;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.entity.effects.CameraShake;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.interpolators.FadeInFloat;
import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.time.Timer;
import org.joml.Vector2f;

public class Camera extends Entity {

	// movement mode(s)
	private int currentMode;
	public static final int INIT = -1, FOLLOW = 0, TARGET = 1, FREE = 2;

	// targets to move to
	private Entity followEntity;
	private Vector2f target, startPosition;

	// point target interpolator
	private final FadeInFloat targetProgress;

	// *** EFFECTS ***
	// SHAKE
	private final CameraShake shakeEffect;
	private Timer shakeTimer;
	private boolean shake;

	// tag if position was corrected last tick
	private boolean wasPositionCorrected;

	public Camera() {
		super(0, 0);
		targetProgress = new FadeInFloat();
		// default follow strength
		currentMode = INIT;
		// create effects
		shakeEffect = new CameraShake(this);
	}


	// GETTERS
	public Entity getFollowEntity() {
		return followEntity;
	}
	public Vector2f getTarget() {
		return target;
	}
	public Vector2f getPositionVec() { return getFinalPosition(); }
	public Vector2f getRawPos() { return new Vector2f(x, y); }
	public int getCurrentMode() { return currentMode; }
	public boolean wasPositionCorrected() { return wasPositionCorrected; }

	// conditions
	public boolean hasReachedTarget() {
		return currentMode == TARGET &&
				(MathUtil.distance(getPositionVec(), target) < 2 || // 2 pixel radius of target
						targetProgress.finished()); // timer done
	}

	// SETTERS
	public void setPosition(Vector2f position) {
		super.setPosition(position);
//		if (currentMode == INIT)
//			tileMap.jumpPosition(position);
	}

	public void setFollowEntity(Entity followEntity) {
		this.followEntity = followEntity;
		if (currentMode == INIT) setPosition(followEntity.getPosition());
		currentMode = FOLLOW;
	}

	public void setTarget(Vector2f newTarget, float time) {
		if (target == null) startPosition = getRawPos();
		else startPosition = target;
		target = newTarget;
		targetProgress.setTime(time);
		targetProgress.start();
		currentMode = TARGET;
		Logger.log("Camera panning to " + target + " from " + getRawPos());
	}

	// ACTIONS
	public void jumpToFollow() {
		if (currentMode == FOLLOW && followEntity != null) {
			setPosition(followEntity.getX(), followEntity.getY());
			fixBounds();
		}
	}

	public void release() {
		followEntity = null;
		target = null;
		currentMode = FREE;
	}

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

	// UPDATE METHODS
	private void doMovement() {
		switch (currentMode) {
			case FOLLOW:
				if (followEntity != null) {
					x = followEntity.getX();
					y = followEntity.getY();
				}
				break;
			case TARGET:
				if (target != null) {
					targetProgress.update();
					Vector2f currentPosition = MathUtil.positionBetween(startPosition, target, targetProgress.getValue());
//					Logger.log("Progress to target: " + targetProgress.getValue() + "; " +
//							"startPosition = " + startPosition + "; " +
//							"currentPosition = " + currentPosition);
					setPosition(currentPosition);
				}
				break;
			case FREE: // don't move
				break;
		}
	}

	private Vector2f getPositionVector() {
		return new Vector2f(DisplayManager.getPrimaryGameWindow().centerX() - x, DisplayManager.getPrimaryGameWindow().centerY() - y);
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

	// MASTER UPDATE
	public void update() {
		// update camera position
		doMovement();
		fixBounds();
		// update effects
		doEffects();
		// set position
		Vector2f position = getFinalPosition();
	}

	@Override
	public void render() {
		// nothing
	}

}
