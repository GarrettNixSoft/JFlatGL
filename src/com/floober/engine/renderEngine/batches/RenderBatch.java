package com.floober.engine.renderEngine.batches;

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
public abstract class RenderBatch {

	protected int layer;

	public RenderBatch(int layer) {
		this.layer = layer;
	}

	public int getLayer() { return layer; }
	public void setLayer(int layer) { this.layer = layer; }

	public abstract void render();

}