package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.renderEngine.elements.RenderElement;
import org.joml.Vector4f;

public abstract class GeometryElement extends RenderElement {

	protected Vector4f color;

	public GeometryElement(float x, float y, float z) {
		super(x, y, z);
	}

	public Vector4f getColor() { return color; }
}
