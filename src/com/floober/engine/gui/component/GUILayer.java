package com.floober.engine.gui.component;

import com.floober.engine.core.util.Logger;
import com.floober.engine.gui.GUI;

import java.util.ArrayList;
import java.util.List;

public class GUILayer extends GUIComponent {

	private final List<GUIComponent> components = new ArrayList<>();

	public GUILayer(String componentID, GUI parent) {
		super(componentID, parent);
	}

	/**
	 * Retrieve all components stored in this layer.
	 * @return an ArrayList of GUIComponents
	 */
	public List<GUIComponent> getAllComponents() {
		List<GUIComponent> allComponents = new ArrayList<>();
		for (GUIComponent component : components) {
			if (component instanceof GUIPanel panel)
				allComponents.addAll(panel.getComponents());
			else
				allComponents.add(component);
		}
		return allComponents;
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
		Logger.log("Opening GUILayer");
		super.open();
		for (GUIComponent component : components) {
			component.open();
		}
	}

	@Override
	public void close() {
		Logger.log("Layer close called");
		super.close();
		for (GUIComponent component : components) {
			component.close();
		}
	}

	@Override
	public void lock() {
		super.lock();
		for (GUIComponent component : components) {
			component.lock();
		}
	}

	@Override
	public void unlock() {
		super.unlock();
		for (GUIComponent component : components) {
			component.unlock();
		}
	}

	@Override
	public void update() {
//		Logger.log("Updating layer " + getComponentID());
		for (GUIComponent component : components) {
			component.updateEvents();
			if (!component.isActive() || component.isLocked()) {
				// ^ this skips updating the panel if: false | x OR x | true is printed from the following log call
				// if the panel is set not active, or if it's locked, it will not be updated or check for input
//				Logger.log("Panel " + panel.getComponentID() + " skipped; ready? " + panel.isActive() + " | locked? " + panel.isLocked());
				continue;
			}
//			Logger.log("Updating panel " + panel.getComponentID());
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

	@Override
	public void remove() {
		for (GUIComponent component : components) {
			component.remove();
		}
	}
}