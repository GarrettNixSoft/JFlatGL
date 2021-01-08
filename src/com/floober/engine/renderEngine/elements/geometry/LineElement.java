package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.util.input.KeyInput;
import org.joml.Vector4f;

public class LineElement extends GeometryElement {

	public LineElement(Vector4f color, float x1, float y1, float x2, float y2, int layer, float lineWidth) {
		super(color, x1, y1, layer, false);
		this.x = x1 - lineWidth / 2;
		this.y = y1 - lineWidth / 2;
		this.width = x2 - x1 + lineWidth;
		this.height = y2 - y1 + lineWidth;
		this.centered = false;
		transform();
	}

	public void setPosition(float x1, float y1, float x2, float y2, float lineWidth) {
		this.x = x1 - lineWidth / 2;
		this.y = y1 - lineWidth / 2;
		this.width = x2 - x1 + lineWidth;
		this.height = y2 - y1 + lineWidth;
	}

}
