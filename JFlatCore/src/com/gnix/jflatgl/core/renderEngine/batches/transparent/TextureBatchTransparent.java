package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.renderers.TextureRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureBatchTransparent extends TransparentBatch {

	private final List<TextureElement> elements = new ArrayList<>();
	private final TextureRenderer renderer;

	public TextureBatchTransparent(int layer, TextureRenderer renderer) {
		super(layer);
		this.renderer = renderer;
	}

	public void addElement(TextureElement element) {
		if (element.textureComponentHasTransparency()) {
			elements.add(element);
		}
		else {
			Logger.logError(Logger.MEDIUM, "Tried to add opaque texture element to transparent batch!");
		}
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public void render() {
		renderer.render(elements, false);
		elements.clear();
	}
}
