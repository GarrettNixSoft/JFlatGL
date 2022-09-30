package com.gnix.jflatgl.core.renderEngine.elements.geometry;

import com.gnix.jflatgl.core.util.color.Colors;
import org.joml.Vector4f;

public class OutlineElement extends GeometryElement {

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	private int roundingMode = HORIZONTAL;

	private float round = 0;
	private float lineWidth;

	// CONSTRUCTORS
	public OutlineElement() {
		super(Colors.INVISIBLE, 0, 0, 0, false);
	}

	public OutlineElement(Vector4f color, float x, float y, int layer, float width, float height, float lineWidth, boolean centered) {
		super(color, x, y, layer, centered);
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
	}

	public OutlineElement(Vector4f color, float x, float y, int layer, float width, float height, float lineWidth, float round, boolean centered) {
		super(color, x, y, layer, centered);
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		this.round = round;
	}

	public OutlineElement(Vector4f color, Vector4f bounds, int layer, float lineWidth, boolean centered) {
		super(color, bounds.x(), bounds.y(), layer, centered);
		this.width = bounds.z() - bounds.x();
		this.height = bounds.w() - bounds.y();
		this.lineWidth = lineWidth;
		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}
	}

	// GETTERS
	public float getLineWidth() {
		return lineWidth;
	}

	public float getRoundRadius() {
		return round;
	}

	/**
	 * Overrides {@code GeometryElement.hasTransparency()} to always return {@code true}.
	 * Outline elements, by their very nature, should always contain transparent pixels.
	 * @return true
	 */
	@Override
	public boolean hasTransparency() {
		return true;
	}

	public int getRoundingMode() {
		return roundingMode;
	}

	public void setRoundingMode(int roundingMode) {
		this.roundingMode = roundingMode;
	}


	// SETTERS
	public void setRoundRadius(float r) {
		this.round = r;
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

}
