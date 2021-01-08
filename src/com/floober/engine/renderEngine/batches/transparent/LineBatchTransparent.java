package com.floober.engine.renderEngine.batches.transparent;

import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.renderEngine.batches.TransparentBatch;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class LineBatchTransparent extends TransparentBatch {

	private final List<LineElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public LineBatchTransparent(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(LineElement element) {
		if (element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add an opaque line element to a transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderLines(elements, false);
	}
}