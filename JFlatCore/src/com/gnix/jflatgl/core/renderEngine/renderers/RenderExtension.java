package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.renderEngine.batches.OpaqueBatch;
import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.util.Layers;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public abstract class RenderExtension {

	// the type of elements to render
	private final Class<? extends RenderElement> type;

	// batches for the element(s)
	private final Map<Integer, OpaqueBatch> opaqueBatches;
	private final Map<Integer, TransparentBatch> transparentBatches;

	public RenderExtension(Class<? extends RenderElement> type, ElementRenderer renderer, Class<? extends OpaqueBatch> opaqueType, Class<? extends TransparentBatch> transparentType) {
		this.type = type;
		opaqueBatches = new HashMap<>();
		transparentBatches = new HashMap<>();
		initLayers(opaqueType, transparentType, renderer);
	}

	private void initLayers(Class<? extends OpaqueBatch> opaqueType, Class<? extends TransparentBatch> transparentType, ElementRenderer renderer) {
		try {
			// fetch constructors
			Constructor<? extends OpaqueBatch> opaqueConstructor = opaqueType.getConstructor(Integer.class, ElementRenderer.class);
			Constructor<? extends TransparentBatch> transparentConstructor = transparentType.getConstructor(Integer.class, ElementRenderer.class);
			// initialize batches
			for (int i = 0; i < Layers.NUM_LAYERS; i++) {
				opaqueBatches.put(i, opaqueConstructor.newInstance(i, renderer));
				transparentBatches.put(i, transparentConstructor.newInstance(i, renderer));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Class<? extends RenderElement> getType() {
		return type;
	}

	public Map<Integer, ? extends OpaqueBatch> getOpaqueBatches() {
		return opaqueBatches;
	}

	public Map<Integer, ? extends TransparentBatch> getTransparentBatches() {
		return transparentBatches;
	}

	public void addElement(RenderElement element) {
		// only accept elements of the correct type
		if (element.getClass() == type) {
			// get the layer this element will be rendered in
			int layer = element.getLayer();
			// add this element to the appropriate batch
			if (element.hasTransparency())
				transparentBatches.get(layer).addElement(element);
			else
				opaqueBatches.get(layer).addElement(element);
		}
	}

}
