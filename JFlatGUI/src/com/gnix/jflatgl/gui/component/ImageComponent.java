package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.gui.GUI;

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
		image.setAlpha(getOpacity());
		image.setHasTransparency(image.textureComponentHasTransparency() || getOpacity() < 1);
		image.setRotation(getRotation());
		image.transform();
	}

	@Override
	public void render() {
		image.render();
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
