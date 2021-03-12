package com.floober.engine.renderEngine.batches.opaque;

import com.floober.engine.renderEngine.batches.OpaqueBatch;
import com.floober.engine.renderEngine.elements.geometry.OutlineElement;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class OutlineBatchOpaque extends OpaqueBatch {

	private final List<OutlineElement> outlineElements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public OutlineBatchOpaque(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(OutlineElement element) {
		if (!element.hasTransparency()) {
			outlineElements.add(element);
		}
		else {
			Logger.logError("Tried to add a transparent outline element to an opaque batch!");
		}
	}

	public void clear() {
		outlineElements.clear();
	}

	@Override
	public void render() {
		renderer.renderOutlines(outlineElements, true);
	}
}
