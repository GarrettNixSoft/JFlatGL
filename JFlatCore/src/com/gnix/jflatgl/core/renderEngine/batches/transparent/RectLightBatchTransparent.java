package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.RectElementLight;
import com.gnix.jflatgl.core.renderEngine.renderers.GeometryRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class RectLightBatchTransparent extends TransparentBatch {

	private final List<RectElementLight> elements = new ArrayList<>();
	private final GeometryRenderer renderer;

	public RectLightBatchTransparent(int layer, GeometryRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(RectElementLight element) {
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
		renderer.renderLightRectangles(elements);
	}

}
