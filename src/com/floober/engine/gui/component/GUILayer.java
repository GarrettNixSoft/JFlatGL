package com.floober.engine.gui.component;

import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GUILayer extends GUIComponent {

	private final List<GUIPanel> panels = new ArrayList<>();

	public GUILayer(String componentID) {
		super(componentID);
	}

	public void addPanel(GUIPanel panel) {
		panels.add(panel);
	}

	@Override
	public boolean isClosed() {
		for (GUIPanel panel : panels) {
			if (!panel.isClosed()) return false;
		}
		return true;
	}

	@Override
	public void open() {
		Logger.log("Opening GUILayer");
		super.open();
		for (GUIPanel panel : panels) {
			panel.open();
		}
	}

	@Override
	public void close() {
		Logger.log("Layer close called");
		super.close();
		for (GUIPanel panel : panels) {
			panel.close();
		}
	}

	@Override
	public void lock() {
		super.lock();
		for (GUIPanel panel : panels) {
			panel.lock();
		}
	}

	@Override
	public void unlock() {
		super.unlock();
		for (GUIPanel panel : panels) {
			panel.unlock();
		}
	}

	@Override
	public void update() {
		for (GUIPanel panel : panels) {
			panel.updateEvents();
			if (!panel.isActive() || panel.isLocked()) {
				Logger.log("Panel " + panel.getComponentID() + " skipped; ready? " + panel.isActive() + " | locked? " + panel.isLocked());
				continue;
			}
			panel.checkInput();
			panel.update();
		}
	}

	@Override
	public void doTransform() {
		for (GUIPanel panel : panels) {
			panel.doTransform();
		}
	}

	@Override
	public void render() {
		for (GUIPanel panel : panels) {
			panel.render();
		}
	}
}