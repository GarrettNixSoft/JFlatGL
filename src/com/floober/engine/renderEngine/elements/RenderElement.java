package com.floober.engine.renderEngine.elements;

import com.floober.engine.display.Display;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class RenderElement implements Comparable<RenderElement> {

	protected float x, y, z;
	protected float width, height;
	protected Vector3f position;
	protected Vector2f scale;

	public RenderElement(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	protected void transform(boolean centered) {
		position = Display.convertToDisplayPosition(x, y, z, width, height, centered);
		scale = Display.convertToDisplayScale(width, height);
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}

	@Override
	public int compareTo(RenderElement other) {
		return Float.compare(other.z, z);
	}

}