package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.display.Display;
import com.floober.engine.util.Logger;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class CircleElement extends GeometryElement {

	private final float innerRadius, outerRadius;
	private final Vector2f center;
	private final Vector2f portion;

	public CircleElement(Vector4f color, float x, float y, float z, float outerRadius) {
		super(x, y, z);
		this.color = color;
		this.innerRadius = 0;
		this.outerRadius = 1;
		this.width = this.height = outerRadius * 2; // quad size = diameter
		transform(true);
		center = new Vector2f(position.x(), position.y());
		portion = new Vector2f(1, 0);
	}

	public CircleElement(Vector4f color, float x, float y, float z, float innerRadius, float outerRadius) {
		super(x, y, z);
		this.color = color;
		this.innerRadius = innerRadius / outerRadius;
		this.outerRadius = 1;
		this.width = this.height = outerRadius * 2; // quad size = diameter
		transform(true);
		center = new Vector2f(position.x(), position.y());
		portion = new Vector2f(1, 0);
	}

	public CircleElement(Vector4f color, float x, float y, float z, float innerRadius, float outerRadius, float portion, float offset) {
		super(x, y, z);
		this.color = color;
		this.innerRadius = innerRadius / outerRadius;
		this.outerRadius = 1;
		this.width = this.height = outerRadius * 2;
		transform(true);
		center = new Vector2f(position.x(), position.y());
		this.portion = new Vector2f(portion, offset);
	}

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

}
