package com.gnix.jflatgl.core.renderEngine.renderers;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;

import java.util.List;

public abstract class ElementRenderer {

	public abstract void renderAllElements(List<? extends RenderElement> elements, boolean depthWritingEnabled);

}
