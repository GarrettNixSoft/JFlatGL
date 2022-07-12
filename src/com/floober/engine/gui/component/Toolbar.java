package com.floober.engine.gui.component;

import com.floober.engine.gui.GUI;

public class Toolbar extends GUIPanel {

	private final Button[] buttons;

	public Toolbar(String componentID, GUI parent, int numButtons) {
		super(componentID, parent);
		buttons = new Button[numButtons];
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
