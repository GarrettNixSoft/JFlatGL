package com.floober.engine.renderEngine.batches.opaque;

import com.floober.engine.renderEngine.batches.OpaqueBatch;
import com.floober.engine.renderEngine.elements.TextureElement;
import com.floober.engine.renderEngine.renderers.TextureRenderer;
import com.floober.engine.util.Logger;

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
		if (!element.getTextureComponent().hasTransparency()) {
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