package com.floober.engine.renderEngine.elements.geometry;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class CircleElement extends GeometryElement {

	private final float innerRadius;
	private final float outerRadius;
	private final Vector2f center;
	private final Vector2f portion;
	private float smoothness;

	public CircleElement(Vector4f color, float x, float y, int layer, float outerRadius) {
		super(color, x, y, layer, true);
		this.innerRadius = 0;
		this.outerRadius = 0.97f;
		this.width = this.height = outerRadius * 2; // quad size = diameter
		transform();
		center = new Vector2f(position.x(), position.y());
		portion = new Vector2f(1, 0);
	}

	public CircleElement(Vector4f color, float x, float y, int layer, float innerRadius, float outerRadius) {
		super(color, x, y, layer, true);
		this.innerRadius = innerRadius / outerRadius;
		this.outerRadius = 0.97f;
		this.width = this.height = outerRadius * 2; // quad size = diameter
		transform();
		center = new Vector2f(position.x(), position.y());
		portion = new Vector2f(1, 0);
	}

	public CircleElement(Vector4f color, float x, float y, int layer, float innerRadius, float outerRadius, float portion, float offset) {
		super(color, x, y, layer, true);
		this.innerRadius = innerRadius / outerRadius;
		this.outerRadius = 0.97f;
		this.width = this.height = outerRadius * 2;
		transform();
		center = new Vector2f(position.x(), position.y());
		this.portion = new Vector2f(portion, offset);
	}

	// GETTERS
	public Vector2f getCenter() {
		return center;
	}

	public float getInnerRadius() {
		return innerRadius;
	}

	public float getOuterRadius() {
		return outerRadius;
	}

	public Vector2f getPortion() { return portion; }

	public float getSmoothness() { return smoothness; }

	// SETTERS

	/**
	 * Set the portion of this circle that will be rendered, as a
	 * percentage of the circle starting from the offset rotation (or
	 * the top if offset is 0) and progressing counterclockwise.
	 * @param portion The percentage of the circle to render.
	 */
	public void setPortion(float portion) {
		this.portion.x = portion;
	}

	/**
	 * Set the starting position for this circle, as a clockwise offset
	 * from the top. The effect of changing this value will only be visible
	 * for circles with a portion set to less than 1.
	 * @param offset The starting offset of the circle.
	 */
	public void setOffset(float offset) {
		this.portion.y = offset;
	}

	/**
	 * Set the amount to smooth the borders of this circle.
	 * @param smoothness The amount of smoothing.
	 */
	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}

	/**
	 * A Circle Element has transparency if it meets one of the
	 * following conditions:
	 * <br>
	 * <br>
	 * 		- Its color has an alpha channel with a value less than 1
	 * <br>
	 * 		- Its inner radius is greater than zero, leaving a transparent center
	 * <br>
	 * 		- Its portion is not 100%, leaving a transparent sector
	 */
	@Override
	public boolean hasTransparency() {
		return super.hasTransparency() ||
				innerRadius != 0 ||
				portion.x() != 1;
	}
}
