package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.renderEngine.elements.RenderElement;
import org.joml.Vector4f;

public abstract class GeometryElement extends RenderElement {

	protected Vector4f color;

	public GeometryElement(Vector4f color, float x, float y, int layer, boolean centered) {
		super(x, y, layer, centered);
		this.color = new Vector4f(color);
	}

	public Vector4f getColor() { return color; }

	public void setColor(Vector4f color) { this.color.set(color); }

	public boolean hasTransparency() {
		return color.w < 1;
	}

}
