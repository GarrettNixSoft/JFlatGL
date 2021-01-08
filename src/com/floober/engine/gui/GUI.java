package com.floober.engine.gui;

import com.floober.engine.gui.component.GUIPanel;

import java.util.ArrayList;
import java.util.List;

public class GUI {

	// addressing this GUI
	private final String id;

	// storing layers: all, and active
	private final List<GUIPanel> panels;

	public GUI(String id) {
		this.id = id;
		panels = new ArrayList<>();
	}

	public void addPanel(GUIPanel panel) {
		panels.add(panel);
	}

	public void open() {
		for (GUIPanel panel : panels) {
			panel.open();
		}
	}

	public void close() {
		for (GUIPanel panel : panels) {
			panel.close();
		}
	}

	public void update() {
		for (GUIPanel panel : panels) {
			panel.updateEvents();
			panel.checkInput();
			panel.update();
		}
	}

	public void render() {
		for (GUIPanel panel : panels) {
			panel.doTransform();
			panel.render();
		}
	}

	public String getId() {
		return id;
	}

}