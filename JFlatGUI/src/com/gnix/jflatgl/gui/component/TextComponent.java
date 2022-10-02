package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.gui.GUI;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TextComponent extends GUIComponent {

	private final GUIText text;
	private float defaultTextSize = 1;
	private boolean enforceTextWidth;

	public TextComponent(String componentID, GUI parent) {
		super(componentID, parent);
		text = new GUIText("Text", defaultTextSize, Game.getFont("default"), new Vector3f(0,0,10), 1, GUIText.Alignment.CENTER);
		text.setWidth(0.5f);
		text.setEdge(0.2f);
		//text components are not clickable by default, but this can be changed with a setter call
		setClickable(false);
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
		setFontSize(fontSize);
		return this;
	}

	public void setFontSize(float fontSize) {
		defaultTextSize = fontSize;
	}

	public void setWidth(float width) {
		text.setWidth(width);
	}

	public void setEdge(float edge) {
		text.setEdge(edge);
	}

	public void setBorderWidth(float borderWidth) {
		text.setBorderWidth(borderWidth);
	}

	public void setBorderEdge(float borderEdge) {
		text.setBorderEdge(borderEdge);
	}

	public void setTextJustify(GUIText.Alignment alignment) {
		text.setTextJustify(alignment);
	}

	public void setTextAnchorPoint(GUIText.AnchorPoint anchorPoint) {
		text.setAnchorPoint(anchorPoint);
	}

	public void setEnforceTextWidth(boolean enforceTextWidth) {
		this.enforceTextWidth = enforceTextWidth;
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
		text.setFontSize(defaultTextSize * getScale());
		text.setColor(getPrimaryColor().mul(getOpacity()));
		text.setOutlineColor(getSecondaryColor().mul(getOpacity()));
		// wrap text?
		if (enforceTextWidth) {
			float textWidth = DisplayManager.convertToScreenSize(getScaledWidth(), false);
			if (text.getLineMaxSize() != textWidth) {
				text.setLineMaxSize(textWidth);
			}
		}
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
