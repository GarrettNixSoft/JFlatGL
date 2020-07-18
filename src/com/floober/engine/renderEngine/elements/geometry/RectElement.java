package com.floober.engine.renderEngine.elements.geometry;

import org.joml.Vector4f;

public class RectElement extends GeometryElement {

	public RectElement(Vector4f color, float x, float y, float z, float width, float height, boolean centered) {
		super(x, y, z);
		this.color = color;
		this.width = width;
		this.height = height;
		transform(centered);
	}

}