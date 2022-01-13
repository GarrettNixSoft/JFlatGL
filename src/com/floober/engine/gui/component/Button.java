package com.floober.engine.gui.component;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.game.Game;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.geometry.RectElement;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.renderEngine.renderers.MasterRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Button extends GUIComponent {

	private final RectElement baseElement;
	private final GUIText label;

	private float defaultTextSize = 1.5f;

	public Button(String componentID) {
		super(componentID);
		label = new GUIText("Button", defaultTextSize, Game.getFont("default"), new Vector3f(0,0,10), 1, true);
		label.setWidth(0.5f);
		label.setEdge(0.2f);
		baseElement = new RectElement(getPrimaryColor(), 0, 0, 0, 0, 0, true);
	}

	public Button label(String labelText) {
		this.label.replaceText(labelText);
		return this;
	}

	public Button font(FontType font) {
		this.label.setFont(font);
		return this;
	}

	public Button textSize(float textSize) {
		defaultTextSize = 1.5f * textSize;
		return this;
	}

	public Button rounded(float roundAmount) {
		baseElement.setRoundRadius(roundAmount);
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
		baseElement.setPosition(getPosition().setComponent(2, getPosition().z() - 1));
		baseElement.setSize(getSize().mul(getScale()));
		baseElement.transform();
		baseElement.setColor(getPrimaryColor().mul(getOpacity()));
		label.setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(getPosition().x(), getPosition().y())),
				MasterRenderer.primaryWindowRenderer.getScreenZ((int) getPosition().z())));
		label.center();
		label.setFontSize(defaultTextSize * getScale());
		label.setColor(getSecondaryColor().mul(getOpacity()));
	}

	@Override
	public void render() {
		if (label.isHidden()) label.show();
		Render.drawRect(baseElement);
	}

}