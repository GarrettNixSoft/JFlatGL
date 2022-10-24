package com.gnix.jflatgl.core.renderEngine.batches;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.renderers.ElementRenderer;

public abstract class TransparentBatch extends RenderBatch<RenderElement> implements Comparable<TransparentBatch> {

	public TransparentBatch(int layer, ElementRenderer renderer) {
		super(layer, renderer);
	}

	@Override
	public int compareTo(TransparentBatch other) {
		return other.layer - this.layer;
	}

}
