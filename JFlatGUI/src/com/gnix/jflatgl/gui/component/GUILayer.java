package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.event.MouseClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public Optional<GUIComponent> getSubcomponent(String componentID) {
		Optional<GUIComponent> result = Optional.empty();
		for (GUIComponent component : components) {
			if (component.getComponentID().equals(componentID)) {
				result = Optional.of(component);
				break;
			}
			else if (component instanceof GUIPanel panel) {
				result = panel.getSubcomponent(componentID);
				if (result.isEmpty())
					result = Optional.empty();
				else break;
			}
		}
		return result;
	}

	public void addComponent(GUIComponent component) {
		if (component != this)
			components.add(component);
	}

	public boolean deleteComponentByID(String componentID) {
		GUIComponent target = null;
		for (GUIComponent component : components) {
			if (component.getComponentID().equals(componentID)) {
				target = component;
			}
		}
		if (target == null) return false;
		else {
			target.remove();
			components.remove(target);
			getParent().unregisterComponent(target);
			return true;
		}
	}

	/**
	 * Set the opacity of all components within this GUILayer
	 * to either 1 or 0, based on the corresponding boolean value
	 * passed.
	 * @param visible {@code true} for 1, {@code false} for 0
	 */
	public void setAllVisible(boolean visible) {
		float opacity = visible ? 1 : 0;
		for (GUIComponent component : components) {
			component.setOpacity(opacity);
		}
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
		Logger.logGUIEvent("Opening GUILayer " + getComponentID());
		super.open();
		for (GUIComponent component : components) {
			component.open();
		}
	}

	@Override
	public void close() {
		Logger.logGUIEvent("Layer close called on " + getComponentID());
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

	/**
	 * Update all components in this layer without calling
	 * {@code checkInput()} on any of them.
	 */
	public void updateNoInput() {
		for (GUIComponent component : components) {
			component.updateEvents();
			if (!component.isActive() || component.isLocked()) {
				// ^ this skips updating the panel if: false | x OR x | true is printed from the following log call
				// if the panel is set not active, or if it's locked, it will not be updated or check for input
//				Logger.log("Panel " + panel.getComponentID() + " skipped; ready? " + panel.isActive() + " | locked? " + panel.isLocked());
				continue;
			}
//			Logger.log("Updating panel " + panel.getComponentID());
			component.update();
		}
	}

	@Override
	public boolean consumeClick(MouseClickEvent clickEvent) {
		// create a consumed flag
		boolean consumed = false;
		// offer the event to each subcomponent
		for (GUIComponent component : components) {
			consumed = component.consumeClick(clickEvent);
			// if the event was consumed, end the loop
			if (consumed) break;
		}
		// report the result
		return consumed;
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
			component.removeComponent();
		}
	}

	@Override
	public void restore() {
		for (GUIComponent component : components) {
			Logger.logGUIInteraction("RESTORING COMPONENT: " + component.getComponentID());
			component.restore();
		}
	}

	@Override
	public String toString() {
		return "GUILayer[\"" + getComponentID() + "\"]";
	}
}
