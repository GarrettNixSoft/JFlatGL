package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.OutlineElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.RectElement;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.util.AnchorPoint;
import com.gnix.jflatgl.gui.GUI;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Button extends GUIComponent {

	private final RectElement baseElement;
	private final OutlineElement outlineElement;
	private final GUIText label;

	private float defaultTextSize = 1.5f;

	public Button(String componentID, GUI parent) {
		super(componentID, parent);
		label = new GUIText("Button", defaultTextSize, Game.getFont("default"), new Vector3f(0,0,10), 1, GUIText.Alignment.CENTER);
		label.setAnchorPoint(AnchorPoint.CENTER);
		label.setWidth(0.5f);
		label.setEdge(0.2f);
		baseElement = new RectElement(getPrimaryColor(), 0, 0, 0, 0, 0, true);
		outlineElement = new OutlineElement(getTertiaryColor(), 0, 0, 0, 0, 0, 0, true);
	}

	public Button label(String labelText) {
		this.label.replaceText(labelText);
		return this;
	}

	public Button font(FontType font) {
		this.label.setFont(font);
		return this;
	}

	public Button fontSize(float textSize) {
		defaultTextSize = 1.5f * textSize;
		return this;
	}

	public Button rounded(float roundAmount) {
		baseElement.setRoundRadius(roundAmount);
		outlineElement.setRoundRadius(roundAmount);
		return this;
	}

	public Button roundMode(int roundMode) {
		baseElement.setRoundingMode(roundMode);
		outlineElement.setRoundingMode(roundMode);
		return this;
	}

	public Button outline(Vector4f outlineColor, float outlineWidth) {
		tertiaryColor(outlineColor);
		outlineElement.setLineWidth(outlineWidth);
		return this;
	}

	@Override
	public void update() {
		// nothing
	}

	public void growOnHover(float growAmount, float time) {
		//
	}

	@Override
	public void doTransform() {
		// transform the base element
		baseElement.setPosition(getPosition().setComponent(2, getPosition().z() - 1));
		baseElement.setSize(getSize().mul(getScale()));
		baseElement.transform();
		baseElement.setColor(getPrimaryColor().mul(getOpacity()));
		// transform the outline element
		outlineElement.setPosition(getPosition().setComponent(2, getPosition().z()));
		outlineElement.setSize(getSize().mul(getScale()));
		outlineElement.transform();
		outlineElement.setColor(getTertiaryColor().mul(getOpacity()));
		// transform the label
		label.setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(getPosition().x(), getPosition().y())), getPosition().z()));
		label.setFontSize(defaultTextSize * getScale());
		label.setColor(getSecondaryColor().mul(getOpacity()));
	}

	@Override
	public void render() {
		if (label.isHidden()) label.show();
		baseElement.render();
		outlineElement.render();
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
