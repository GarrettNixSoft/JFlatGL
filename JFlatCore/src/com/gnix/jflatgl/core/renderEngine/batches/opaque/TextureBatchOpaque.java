package com.gnix.jflatgl.core.renderEngine.batches.opaque;

import com.gnix.jflatgl.core.renderEngine.batches.OpaqueBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.renderers.TextureRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureBatchOpaque extends OpaqueBatch {

	private final TextureRenderer renderer;

	public TextureBatchOpaque(int layer, TextureRenderer renderer) {
		super(layer, renderer);
		this.renderer = renderer;
	}

	public void addElement(RenderElement element) {
		if (element instanceof TextureElement texture) {
			if (!texture.textureComponentHasTransparency()) {
				elements.add(texture);
			}
			else {
				Logger.logError("Tried to add Texture with transparency to an opaque batch!");
			}
		}
	}

	@Override
	public void render() {
		renderer.renderAllElements(elements, true);
		elements.clear();
	}

}
