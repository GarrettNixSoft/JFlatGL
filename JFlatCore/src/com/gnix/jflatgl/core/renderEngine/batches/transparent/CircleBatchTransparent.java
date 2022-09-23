package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.CircleElement;
import com.gnix.jflatgl.core.renderEngine.renderers.GeometryRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class CircleBatchTransparent extends TransparentBatch {

	private final List<CircleElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public CircleBatchTransparent(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(CircleElement element) {
		if (element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add an opaque circle element to a transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderCircles(elements, false);
	}
}