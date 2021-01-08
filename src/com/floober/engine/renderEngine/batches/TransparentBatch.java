package com.floober.engine.renderEngine.batches;

public abstract class TransparentBatch extends RenderBatch implements Comparable<TransparentBatch> {

	public TransparentBatch(int layer) {
		super(layer);
	}

	@Override
	public int compareTo(TransparentBatch other) {
		return other.layer - this.layer;
	}

}
