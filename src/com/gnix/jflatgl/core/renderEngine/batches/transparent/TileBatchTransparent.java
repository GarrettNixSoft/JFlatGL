package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.tile.TileElement;
import com.gnix.jflatgl.core.renderEngine.renderers.TileRenderer;
import com.gnix.jflatgl.core.renderEngine.textures.TextureAtlas;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileBatchTransparent extends TransparentBatch {

	private final Map<TextureAtlas, List<TileElement>> elements = new HashMap<>();
	private final TileRenderer renderer;

	public TileBatchTransparent(int layer, TileRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(TileElement element) {
		if (element.hasTransparency()) {
			List<TileElement> elementList = elements.computeIfAbsent(element.getTextureAtlas(), k -> new ArrayList<>());
			elementList.add(element);
		}
		else {
			Logger.logError("Tried to add an opaque tile element to a transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.render(elements, false);
	}
}
