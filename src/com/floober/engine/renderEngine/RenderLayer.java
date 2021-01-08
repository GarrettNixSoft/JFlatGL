package com.floober.engine.renderEngine;

import com.floober.engine.renderEngine.batches.OpaqueBatch;
import com.floober.engine.renderEngine.batches.TransparentBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderLayer {

	private final List<OpaqueBatch> opaqueBatches = new ArrayList<>();
	private final List<TransparentBatch> transparentBatches = new ArrayList<>();

	/**
	 * Add an opaque batch to this layer for this frame.
	 * @param batch The batch to add.
	 */
	public void addOpaqueBatch(OpaqueBatch batch) {
		opaqueBatches.add(batch);
	}

	/**
	 * Add a batch with transparency to this layer for this frame.
	 * @param batch The batch to add.
	 */
	public void addTransparentBatch(TransparentBatch batch) {
		transparentBatches.add(batch);
	}

	/**
	 * Sorts the current opaque batches, calls their {@code render()} methods
	 * in order, then clears the list.
	 */
	public void renderOpaque() {
		Collections.sort(opaqueBatches);
		for (OpaqueBatch opaqueBatch : opaqueBatches) {
			opaqueBatch.render();
		}
		opaqueBatches.clear();
	}

	/**
	 * Sorts the current transparent batches, calls their {@code render()} methods
	 * in order, then clears the list.
	 */
	public void renderTransparent() {
		Collections.sort(transparentBatches);
		for (TransparentBatch transparentBatch : transparentBatches) {
			transparentBatch.render();
		}
		transparentBatches.clear();
	}

}