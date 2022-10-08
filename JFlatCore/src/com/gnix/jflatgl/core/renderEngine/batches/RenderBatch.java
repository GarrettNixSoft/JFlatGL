package com.gnix.jflatgl.core.renderEngine.batches;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A RenderBatch represents a batch of elements to be rendered
 * at once with a single shader bind.
 *
 * Each batch has a z-layer. These represent their relative positions
 * within the RenderLayer. The MasterRenderer will group together
 * batches with z-layers in certain ranges into RenderLayers, which
 * have sub-layers. RenderLayers are rendered in order, first by
 * rendering all opaque batches in each RenderLayer, then by rendering
 * all transparent batches in each RenderLayer.
 */
public abstract class RenderBatch<T extends RenderElement> {

	protected ElementRenderer renderer;
	protected int layer;

	protected List<RenderElement> elements = new ArrayList<>();

	public RenderBatch(int layer, ElementRenderer renderer) {
		this.renderer = renderer;
		this.layer = layer;
	}

	public int getLayer() { return layer; }
	public void setLayer(int layer) { this.layer = layer; }

	public void clear() {
		elements.clear();
	}

	public abstract void addElement(T element);

	public abstract void render();

}
