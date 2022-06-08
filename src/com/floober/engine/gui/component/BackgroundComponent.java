package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.gui.GUI;
import org.joml.Vector4f;

public class BackgroundComponent extends GUIComponent {

	private final Vector4f color;

	/**
	 * Construct a BackgroundComponent.
	 * @param componentID this component's ID
	 * @param parent the top-level GUI parent of this component
	 */
	public BackgroundComponent(String componentID, GUI parent, Vector4f color) {
		super(componentID, parent);
		this.color = color;
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void doTransform() {
		// nothing
	}

	@Override
	public void render() {
		Render.fillScreen(color, Layers.BOTTOM_LAYER);
	}

	@Override
	public void remove() {
		// nothing
	}
}
