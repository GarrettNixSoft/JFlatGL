package com.floober.engine.renderEngine.elements.geometry;

import org.joml.Vector4f;

import java.awt.*;

public class RectElement extends GeometryElement {

	private float r = 0;

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
		this.r = r;
		transform();
	}

	public RectElement(Vector4f color, Vector4f bounds, int layer, boolean centered) {
		super(color, bounds.x, bounds.y, layer, centered);
		this.width = bounds.z - bounds.x;
		this.height = bounds.w - bounds.y;
		transform();
	}

	public void setRoundRadius(float r) {
		this.r = r;
	}

	public float getRoundRadius() {
		return r;
	}

	public Rectangle getRectangle() {
		if (centered)
			return new Rectangle((int) (x - width / 2), (int) (y - height / 2), (int) width, (int) height);
		else
			return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}

}