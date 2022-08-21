package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.gui.GUI;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ImageButton extends Button {

	private final TextureElement image;
	private final TextureElement hoverImage;
	private final TextureElement labelImage;

	private float labelScale = 1;

	public ImageButton(String componentID, GUI parent) {
		super(componentID, parent);
		image = new TextureElement();
		image.setCentered(true);
		hoverImage = new TextureElement();
		hoverImage.setCentered(true);
		labelImage = new TextureElement();
		labelImage.setCentered(true);
	}

	public ImageButton labelScale(float labelScale) {
		setLabelScale(labelScale);
		return this;
	}

	@Override
	public ImageButton location(float x, float y, int layer) {
		super.location(x, y, layer);
		image.setPosition(x, y, layer);
		hoverImage.setPosition(x, y, layer);
		labelImage.setPosition(x, y, layer);
		return this;
	}

	@Override
	public ImageButton size(float width, float height) {
		super.size(width, height);
		image.setSize(width, height);
		hoverImage.setSize(width, height);
		labelImage.setSize(width, height);
		return this;
	}

	@Override
	public void setPosition(Vector3f position) {
		image.setPosition(position);
		hoverImage.setPosition(position);
		labelImage.setPosition(position);
		super.setPosition(position);
	}

	@Override
	public void setPosition(float x, float y, int layer) {
		image.setPosition(x, y, layer);
		hoverImage.setPosition(x, y, layer);
		labelImage.setPosition(x, y, layer);
		super.setPosition(x, y, layer);
	}

	@Override
	public void setPosition(Vector2f position, float z) {
		image.setPosition(new Vector3f(position, z));
		hoverImage.setPosition(new Vector3f(position, z));
		labelImage.setPosition(new Vector3f(position, z));
		super.setPosition(position, z);
	}

	public ImageButton image(TextureComponent tex) {
		setImage(tex);
		return this;
	}

	public ImageButton hoverImage(TextureComponent tex) {
		setHoverImage(tex);
		return this;
	}

	public ImageButton labelImage(TextureComponent tex) {
		setLabelImage(tex);
		return this;
	}

	public void setImage(TextureComponent tex) {
		image.setTexture(tex);
	}

	public void setHoverImage(TextureComponent tex) {
		hoverImage.setTexture(tex);
	}

	public void setLabelImage(TextureComponent tex) {
		labelImage.setTexture(tex);
	}

	public void setLabelScale(float labelScale) {
		this.labelScale = labelScale;
	}

	public void setImageRotation(float rotation) {
		image.setRotation(rotation);
		hoverImage.setRotation(rotation);
		labelImage.setRotation(rotation);
	}

	@Override
	public void doTransform() {
		image.setSize(getScaledSize());
		image.transform();
		hoverImage.setSize(getScaledSize());
		hoverImage.transform();
		labelImage.setSize(getScaledSize().mul(labelScale));
		labelImage.transform();
	}

	@Override
	public void render() {
		if (mouseHover() && !isLocked()) hoverImage.render();
		else image.render();
		labelImage.render();
	}

	@Override
	public void restore() {
		// nothing
	}

}
