package com.gnix.jflatgl.render.batch;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.element.TileElement;

public class TileBatchTransparent extends TransparentBatch {

	public TileBatchTransparent(Integer layer, ElementRenderer renderer) {
		super(layer, renderer);
	}

	public void addElement(RenderElement element) {
		if (element instanceof TileElement tile) {
			if (tile.hasTransparency()) {
				elements.add(tile);
			}
			else {
				Logger.logError("Tried to add an opaque tile element to a transparent batch!");
			}
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.renderAllElements(elements, false);
	}
}
