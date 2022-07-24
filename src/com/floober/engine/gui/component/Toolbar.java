package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.gui.GUI;
import org.joml.Vector4f;

public class Toolbar extends GUIPanel {

	private final RectElement baseElement;
	private final Button[] buttons;
	private int selected;

	public Toolbar(String componentID, GUI parent, int numButtons) {
		super(componentID, parent);
		baseElement = new RectElement(Colors.INVISIBLE, new Vector4f(), getLayer(), true);
		buttons = new Button[numButtons];
	}

	@Override
	public GUIComponent primaryColor(Vector4f primaryColor) {
		baseElement.setColor(primaryColor);
		return super.primaryColor(primaryColor);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		baseElement.setSize(width, height);
	}

	@Override
	public void setPosition(float x, float y, int layer) {
		baseElement.setPosition(x, y, layer);
		super.setPosition(x, y, layer);
	}

	public void sizeButtons(float width, float height) {
		for (Button button : buttons) {
			button.setSize(width, height);
		}
	}

	public void positionButtons(int padding) {
		int buttonSize = (int) buttons[0].getWidth();
		// position each button
		for (int i = 0; i < buttons.length; i++) {
			int xPos = (int) getLeft() + buttonSize / 2 + padding + i * (buttonSize + padding);
			int yPos = (int) getY();
			buttons[i].setPosition(xPos, yPos, getLayer() + 1);
		}
	}

	public Button[] getButtons() {
		return buttons;
	}

	public void assignButton(int index, Button button) {
		buttons[index] = button;
		addComponent(button);
	}

	public Button getButton(int index) {
		return buttons[index];
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	@Override
	public void doTransform() {
		baseElement.setSize(getScaledSize());
		baseElement.transform();
		super.doTransform();
	}

	@Override
	public void render() {
		super.render();
		baseElement.render();
	}
}
