package com.gnix.jflatgl.core.renderEngine.batches.opaque;

import com.gnix.jflatgl.core.renderEngine.batches.OpaqueBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.GeometryElement;
import com.gnix.jflatgl.core.renderEngine.renderers.GeometryRenderer;
import com.gnix.jflatgl.core.util.Logger;

public class GeometryBatchOpaque extends OpaqueBatch {

	public GeometryBatchOpaque(int layer, GeometryRenderer renderer) {
		super(layer, renderer);
	}

	@Override
	public void addElement(RenderElement element) {
		if (element instanceof GeometryElement geom) {
			if (!geom.hasTransparency()) {
				elements.add(geom);
			}
			else {
				Logger.logError("Tried to add transparent geometry element to an opaque batch!");
			}
		}
	}

	@Override
	public void render() {
		renderer.renderAllElements(elements, true);
	}
}
