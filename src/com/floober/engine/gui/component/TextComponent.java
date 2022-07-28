package com.floober.engine.gui.component;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.gui.GUI;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TextComponent extends GUIComponent {

	private final GUIText text;
	private float defaultTextSize = 1.5f;

	public TextComponent(String componentID, GUI parent) {
		super(componentID, parent);
		text = new GUIText("Text", defaultTextSize, Game.getFont("default"), new Vector3f(0,0,10), 1, true);
		text.setWidth(0.5f);
		text.setEdge(0.2f);
	}

	public TextComponent text(String textStr) {
		text.replaceText(textStr);
		return this;
	}

	public TextComponent font(FontType font) {
		text.setFont(font);
		return this;
	}

	public TextComponent fontSize(float fontSize) {
		defaultTextSize = fontSize;
		return this;
	}

	public GUIText getText() {
		return text;
	}

	@Override
	public void update() {
		// nothing
	}

	@Override
	public void doTransform() {
		// transform the label
		text.setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(getPosition().x(), getPosition().y())), getPosition().z()));
		text.center();
		text.setFontSize(defaultTextSize * getScale());
		text.setColor(getPrimaryColor().mul(getOpacity()));
		text.setOutlineColor(getSecondaryColor().mul(getOpacity()));
	}

	@Override
	public void render() {
		if (text.isHidden()) text.show();
	}

	@Override
	public void remove() {
		text.remove();
	}

	@Override
	public void restore() {
		text.show();
	}
}
