package com.gnix.jflatgl.render;

import com.gnix.jflatgl.core.renderEngine.renderers.RenderExtension;
import com.gnix.jflatgl.element.TileElement;
import com.gnix.jflatgl.render.batch.TileBatchOpaque;
import com.gnix.jflatgl.render.batch.TileBatchTransparent;

public class TileRenderExtension extends RenderExtension {

	public TileRenderExtension() {
		super(TileElement.class, new TileRenderer(), TileBatchOpaque.class, TileBatchTransparent.class);
	}

}
