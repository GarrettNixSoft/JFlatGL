package com.floober.engine.renderEngine.elements.geometry;

import org.joml.Vector4f;

public class OutlineElement extends GeometryElement {

	private final LineElement[] lines;
	private final float lineWidth;

	public OutlineElement(Vector4f color, float x, float y, int layer, float width, float height, float lineWidth, boolean centered) {
		super(color, x, y, layer, centered);
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		if (centered) {
			x -= width / 2;
			y -= height / 2;
		}
		lines = new LineElement[4];
		lines[0] = new LineElement(color, x, y, x + width, y, layer, lineWidth); // top
		lines[1] = new LineElement(color, x + width, y, x + width, y + height, layer, lineWidth); // right
		lines[2] = new LineElement(color, x + width, y + height, x, y + height, layer, lineWidth); // bottom
		lines[3] = new LineElement(color, x, y + height + lineWidth, x, y, layer, lineWidth); // left
		// (for some reason I need to add lineWidth to the bottom of the left-side line when lineWidth is > 1, or else the bottom-left corner will be missing
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
		lines = new LineElement[4];
		lines[0] = new LineElement(color, x, y, x + width, y, layer, lineWidth); // top
		lines[1] = new LineElement(color, x + width, y, x + width, y + height, layer, lineWidth); // right
		lines[2] = new LineElement(color, x + width, y + height, x, y + height, layer, lineWidth); // bottom
		lines[3] = new LineElement(color, x, y + height + lineWidth, x, y, layer, lineWidth); // left
		// (for some reason I need to add lineWidth to the bottom of the left-side line when lineWidth is > 1, or else the bottom-left corner will be missing
	}

	public LineElement[] getLines() {
		return lines;
	}

	@Override
	public void setColor(Vector4f color) {
		super.setColor(color);
		for (LineElement line : lines) {
			line.setColor(color);
		}
	}

	@Override
	public void setPosition(float x, float y, int layer) {
		this.x = x;
		this.y = y;
		this.layer = layer;
		if (centered) {
			this.x -= width / 2;
			this.y -= height / 2;
		}
		lines[0].setPosition(x, y, x + width, y, lineWidth); // top
		lines[1].setPosition(x + width, y, x + width, y + height, lineWidth); // right
		lines[2].setPosition(x + width, y + height, x, y + height, lineWidth); // bottom
		lines[3].setPosition(x, y + height + lineWidth, x, y, lineWidth); // left
	}

	@Override
	public void transform() {
		super.transform();
		for (LineElement line : lines)
			line.transform();
	}

}
