package com.gnix.jflatgl.core.renderEngine.batches.opaque;

import com.gnix.jflatgl.core.renderEngine.batches.OpaqueBatch;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.CircleElement;
import com.gnix.jflatgl.core.renderEngine.renderers.GeometryRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class CircleBatchOpaque extends OpaqueBatch {

	private final List<CircleElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public CircleBatchOpaque(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(CircleElement element) {
		if (!element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add transparent circle element to an opaque batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderCircles(elements, true);
	}
}
