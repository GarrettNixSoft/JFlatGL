package com.floober.engine.renderEngine.batches.transparent;

import com.floober.engine.renderEngine.batches.TransparentBatch;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class OutlineBatchTransparent extends TransparentBatch {

	private final List<OutlineElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public OutlineBatchTransparent(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(OutlineElement element) {
		if (element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add an opaque outline element to a transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderOutlines(elements, false);
	}
}
