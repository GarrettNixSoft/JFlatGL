package com.floober.engine.core.renderEngine.elements.geometry;

import org.joml.Vector4f;

import java.awt.*;

public class RectElement extends GeometryElement {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	private int roundingMode = HORIZONTAL;

	private float round = 0;

	public RectElement(Vector4f color, float x, float y, int layer, float width, float height, boolean centered) {
		super(color, x, y, layer, centered);
		this.width = width;
		this.height = height;
		transform();
	}

	public RectElement(Vector4f color, float x, float y, int layer, float width, float height, float r, boolean centered) {
		super(color, x, y, layer, centered);
		this.width = width;
		this.height = height;
		this.round = r;
		transform();
	}

	public RectElement(Vector4f color, Vector4f bounds, int layer, boolean centered) {
		super(color, bounds.x, bounds.y, layer, centered);
		this.width = bounds.z - bounds.x;
		this.height = bounds.w - bounds.y;
		transform();
	}

	public void setRoundRadius(float r) {
		this.round = r;
	}

	public float getRoundRadius() {
		return round;
	}

	public int getRoundingMode() {
		return roundingMode;
	}

	public void setRoundingMode(int roundingMode) {
		this.roundingMode = roundingMode;
	}

	public Rectangle getRectangle() {
		if (centered)
			return new Rectangle((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
		else
			return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

}