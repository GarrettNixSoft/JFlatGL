package com.floober.engine.renderEngine.batches.opaque;

import com.floober.engine.renderEngine.batches.OpaqueBatch;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class RectBatchOpaque extends OpaqueBatch {

	private final List<RectElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public RectBatchOpaque(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(RectElement element) {
		if (!element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add transparent rect element to an opaque batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderRectangles(elements, true);
	}
}
