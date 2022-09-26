package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.core.util.math.MathUtil;
import com.gnix.jflatgl.core.util.time.Timer;
import com.gnix.jflatgl.gui.component.GUIComponent;

public class MoveComponentEvent extends GUIEvent {

	private final Timer timer;
	private final float startX, startY;
	private final float targetX, targetY;

	public MoveComponentEvent(boolean blocking, GUIComponent targetComponent, float targetX, float targetY, float time) {
		super(blocking, targetComponent);
		startX = targetComponent.getX();
		startY = targetComponent.getY();
		this.targetX = targetX;
		this.targetY = targetY;
		timer = new Timer(time);
	}

	@Override
	public void onStart() {
		timer.start();
	}

	@Override
	public void update() {
		if (timer.finished()) complete = true;
		else {
			float progress = timer.getProgress();
			float xPos = MathUtil.interpolate(startX, targetX, progress);
			float yPos = MathUtil.interpolate(startY, targetY, progress);
			targetComponent.setPosition(xPos, yPos, targetComponent.getLayer());
		}

	}

	@Override
	public void onFinish() {
		targetComponent.setPosition(targetX, targetY, targetComponent.getLayer());
	}
}
