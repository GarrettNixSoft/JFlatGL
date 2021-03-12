package com.floober.engine.renderEngine.batches.transparent;

import com.floober.engine.renderEngine.batches.TransparentBatch;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.renderers.TextureRenderer;
import com.floober.engine.util.Logger;

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
			Logger.logError("Tried to add opaque texture element to transparent batch!", Logger.MEDIUM);
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
