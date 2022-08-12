package com.floober.engine.gui.component.tab;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.component.GUIComponent;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TabButton extends GUIComponent {

	private final TextureElement iconTexture;
	private final GUIText label;

	private float defaultTextSize = 1.5f;

	public TabButton(String componentID, GUI parent, TextureComponent icon) {
		super(componentID, parent);
		label = new GUIText("Tab", defaultTextSize, Game.getFont("default"), new Vector3f(0,0,10), 1, GUIText.Justify.CENTER);
		label.setCenteredVertical(true);
		iconTexture = new TextureElement();
		iconTexture.setTexture(icon);
	}

	public TabButton label(String labelText) {
		this.label.replaceText(labelText);
		return this;
	}

	public TabButton font(FontType font) {
		this.label.setFont(font);
		return this;
	}

	public TabButton textSize(float textSize) {
		defaultTextSize = 1.5f * textSize;
		return this;
	}

	@Override
	public void setPosition(Vector3f position) {
		label.setPosition(position);
		super.setPosition(position);
	}

	@Override
	public void setPosition(float x, float y, int layer) {
		label.setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(getPosition().x(), getPosition().y() + getSize().y() / 2)), getPosition().z()));
		super.setPosition(x, y, layer);
	}

	@Override
	public void setPosition(Vector2f position, float z) {
		label.setPosition(new Vector3f(position, z));
		super.setPosition(position, z);
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void doTransform() {
		iconTexture.setPosition(getPosition().setComponent(2, getPosition().z() - 1));
		iconTexture.setSize(getSize().mul(getScale()));
		iconTexture.transform();
		// transform the label
		label.setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(getPosition().x(), getPosition().y() + getSize().y() / 2)), getPosition().z()));
		label.center();
		label.setFontSize(defaultTextSize * getScale());
		label.setColor(getSecondaryColor().mul(getOpacity()));
	}

	@Override
	public void render() {
		if (label.isHidden()) label.show();
		Render.drawImage(iconTexture);
	}

	@Override
	public void remove() {
		label.remove();
	}

	@Override
	public void restore() {
		label.show();
	}

}