package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.gui.GUI;

public class ImageComponent extends GUIComponent {

	private final TextureElement image;

	public ImageComponent(String componentID, GUI parent, TextureComponent texture) {
		super(componentID, parent);
		image = new TextureElement(texture);
	}

	public void setImage(TextureComponent texture) {
		image.setTexture(texture);
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void doTransform() {
		image.setPosition(getPosition());
		image.setSize(getScaledSize());
		image.transform();
	}

	@Override
	public void render() {
		// nothing
	}

	@Override
	public void remove() {
		// nothing
	}

	@Override
	public void restore() {
		// nothing
	}
}
