package com.floober.engine.renderEngine.batches.transparent;

import com.floober.engine.renderEngine.batches.TransparentBatch;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class RectBatchTransparent extends TransparentBatch {

	private final List<RectElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public RectBatchTransparent(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(RectElement element) {
		if (element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add opaque rect element to a transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderRectangles(elements, false);
	}
}
