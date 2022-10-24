package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.GeometryElement;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;
import com.gnix.jflatgl.core.util.Logger;

public class GeometryBatchTransparent extends TransparentBatch {

	public GeometryBatchTransparent(int layer, ElementRenderer renderer) {
		super(layer, renderer);
	}

	@Override
	public void addElement(RenderElement element) {
		if (element instanceof GeometryElement geom) {
			if (geom.hasTransparency()) {
				elements.add(geom);
			}
			else {
				Logger.logError("Tried to add opaque geometry element to a transparent batch!");
			}
		}
	}

	@Override
	public void render() {
		renderer.renderAllElements(elements, false);
	}
}
