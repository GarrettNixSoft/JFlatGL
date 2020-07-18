package com.floober.engine.renderEngine.elements.geometry;

import com.floober.engine.renderEngine.elements.RenderElement;
import org.joml.Vector4f;

public class OutlineElement extends GeometryElement {

	private final LineElement[] lines;

	public OutlineElement(Vector4f color, float x, float y, float z, float width, float height, float lineWidth, boolean centered) {
		super(x, y, z);
		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}
		lines = new LineElement[4];
		lines[0] = new LineElement(color, x, y, x + width, y, z, lineWidth); // top
		lines[1] = new LineElement(color, x + width, y, x + width, y + height, z, lineWidth); // right
		lines[2] = new LineElement(color, x + width, y + height, x, y + height, z, lineWidth); // bottom
		lines[3] = new LineElement(color, x, y + height + lineWidth, x, y, z, lineWidth); // left
		// (for some reason I need to add lineWidth to the bottom of the left-side line when lineWidth is > 1, or else the bottom-left corner will be missing
	}

	public LineElement[] getLines() {
		return lines;
	}

}
