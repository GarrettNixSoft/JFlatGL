package com.floober.engine.renderEngine.batches.opaque;

import com.floober.engine.renderEngine.batches.OpaqueBatch;
import com.floober.engine.renderEngine.elements.geometry.LineElement;
import com.floober.engine.renderEngine.renderers.GeometryRenderer;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class LineBatchOpaque extends OpaqueBatch {

	private final List<LineElement> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public LineBatchOpaque(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(LineElement element) {
		if (!element.hasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add a transparent line element to an opaque batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderLines(elements, true);
	}
}
