package com.floober.engine.renderEngine.elements.geometry;

import org.joml.Vector4f;

public class LineElement extends GeometryElement {

	public LineElement(Vector4f color, float x1, float y1, float x2, float y2, float z, float lineWidth) {
		super(x1, y1, z);
		this.color = color;
		this.x = x1 - lineWidth / 2;
		this.y = y1 - lineWidth / 2;
		this.width = x2 - x1 + lineWidth;
		this.height = y2 - y1 + lineWidth;
		transform(false);
	}
}
