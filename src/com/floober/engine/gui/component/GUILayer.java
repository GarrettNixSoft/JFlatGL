package com.floober.engine.gui.component;

import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GUILayer extends GUIComponent {

	private final List<GUIComponent> components = new ArrayList<>();

	public GUILayer(String componentID) {
		super(componentID);
	}

	public void addComponent(GUIComponent component) {
		components.add(component);
	}

	@Override
	public boolean isClosed() {
		for (GUIComponent component : components) {
			if (!component.isClosed()) return false;
		}
		return true;
	}

	@Override
	public void open() {
		super.open();
		Logger.log("Opening GUILayer");
		for (GUIComponent component : components) {
			component.open();
		}
	}

	@Override
	public void close() {
		for (GUIComponent component : components) {
			component.close();
		}
	}

	@Override
	public void update() {
		for (GUIComponent component : components) {
			component.updateEvents();
			if (!component.isReady()) {
				Logger.log("Component " + component + " is not ready, skipping");
				continue;
			}
			component.checkInput();
			component.update();
		}
	}

	@Override
	public void doTransform() {
		for (GUIComponent component : components) {
			component.doTransform();
		}
	}

	@Override
	public void render() {
		for (GUIComponent component : components) {
			component.render();
		}
	}
}