package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.gui.GUI;

public class ImageButton extends Button {

	private final TextureElement image;
	private final TextureElement hoverImage;

	public ImageButton(String componentID, GUI parent) {
		super(componentID, parent);
		image = new TextureElement();
		image.setCentered(true);
		hoverImage = new TextureElement();
		hoverImage.setCentered(true);
	}

	public ImageButton image(TextureComponent tex) {
		image.setTexture(tex);
		return this;
	}

	public ImageButton hoverImage(TextureComponent tex) {
		hoverImage.setTexture(tex);
		return this;
	}

	@Override
	public ImageButton location(float x, float y, int layer) {
		super.location(x, y, layer);
		image.setPosition(x, y, layer);
		hoverImage.setPosition(x, y, layer);
		return this;
	}

	@Override
	public ImageButton size(float width, float height) {
		super.size(width, height);
		image.setSize(width, height);
		hoverImage.setSize(width, height);
		return this;
	}

	@Override
	public void doTransform() {
		image.transform();
		hoverImage.transform();
	}

	@Override
	public void render() {
		if (mouseOver()) hoverImage.render();
		else image.render();
	}

}
