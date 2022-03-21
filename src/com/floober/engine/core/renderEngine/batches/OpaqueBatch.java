package com.floober.engine.core.renderEngine.batches;

public abstract class OpaqueBatch extends RenderBatch implements Comparable<OpaqueBatch> {

	public OpaqueBatch(int layer) {
		super(layer);
	}

	@Override
	public int compareTo(OpaqueBatch other) {
		return this.layer - other.layer;
	}


}