package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.gui.GUI;
import org.joml.Vector4f;

public class Toolbar extends GUIPanel {

	private final RectElement baseElement;
	private final Button[] buttons;

	public Toolbar(String componentID, GUI parent, int numButtons) {
		super(componentID, parent);
		baseElement = new RectElement(Colors.INVISIBLE, new Vector4f(), getLayer(), false);
		buttons = new Button[numButtons];
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		baseElement.setSize(width, height);
	}

	public void sizeButtons(float width, float height) {
		for (Button button : buttons) {
			button.setSize(width, height);
		}
	}

	public void positionButtons(int padding) {
		// determine how much horizontal spacing is available to the buttons
		int buttonSpace = (int) (getWidth() / buttons.length);
		int buttonSize = (int) buttons[0].getWidth();
		// position each button
		for (int i = 0; i < buttons.length; i++) {
			int xPos = i * (buttonSize + padding) + padding;
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

}
