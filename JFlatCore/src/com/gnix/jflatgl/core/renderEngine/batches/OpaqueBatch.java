package com.gnix.jflatgl.core.renderEngine.batches;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;

public abstract class OpaqueBatch extends RenderBatch<RenderElement> implements Comparable<OpaqueBatch> {

	public OpaqueBatch(int layer, ElementRenderer renderer) {
		super(layer, renderer);
	}

	@Override
	public int compareTo(OpaqueBatch other) {
		return this.layer - other.layer;
	}


}
