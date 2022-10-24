package com.gnix.jflatgl.core.renderEngine.batches.transparent;

import com.gnix.jflatgl.core.renderEngine.batches.TransparentBatch;
import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.renderers.TextureRenderer;
import com.gnix.jflatgl.core.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class TextureBatchTransparent extends TransparentBatch {

	private final TextureRenderer renderer;

	public TextureBatchTransparent(int layer, TextureRenderer renderer) {
		super(layer, renderer);
		this.renderer = renderer;
	}

	public void addElement(RenderElement element) {
		if (element instanceof TextureElement texture) {
			if (texture.textureComponentHasTransparency()) {
				elements.add(texture);
			}
			else {
				Logger.logError(Logger.MEDIUM, "Tried to add opaque texture element to transparent batch!");
			}
		}

	}

	@Override
	public void render() {
		renderer.renderAllElements(elements, false);
		elements.clear();
	}

}
