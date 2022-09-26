package com.gnix.jflatgl.core.renderEngine.batches.opaque;

import com.gnix.jflatgl.core.renderEngine.batches.OpaqueBatch;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.renderers.TextureRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureBatchOpaque extends OpaqueBatch {

	private final List<TextureElement> elements = new ArrayList<>();
	private final TextureRenderer renderer;

	public TextureBatchOpaque(int layer, TextureRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(TextureElement element) {
		if (!element.textureComponentHasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError("Tried to add Texture with transparency to an opaque batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.render(elements, true);
		elements.clear();
	}

}
