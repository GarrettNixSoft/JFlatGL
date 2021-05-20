package com.floober.engine.gui.component;

import com.floober.engine.gui.event.ClosedEvent;
import com.floober.engine.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class GUIPanel extends GUIComponent {

	private final List<GUIComponent> components = new ArrayList<>();

	public GUIPanel(String componentID) {
		super(componentID);
	}

	public void addComponent(GUIComponent component) {
		components.add(component);
	}

	@Override
	public boolean isClosed() {
		if (components.isEmpty()) return false;
 		for (GUIComponent component : components) {
			if (!component.isClosed()) {
//				Logger.log("Panel not closed; component " + component.getComponentID() + " is still open");
				return false;
			}
		}
// 		Logger.log("Panel closed!");
		return !hasPendingEvents();
	}

	@Override
	public boolean isLocked() {
		if (components.isEmpty()) return false;
		for (GUIComponent component : components) {
			if (!component.isLocked()) return false;
		}
		return true;
	}

	@Override
	public boolean hasPendingEvents() {
		for (GUIComponent component : components) {
			if (component.hasPendingEvents()) {
				// TODO THIS is where the bug is coming from; why does play_button not report that it's waiting on a ClosedEvent?
				return true;
			}
		}
		return super.hasPendingEvents();
	}

	@Override
	public boolean hasPendingEvents(ClosedEvent event) {
		for (GUIComponent component : components) {
			if (component.hasPendingEvents(event)) {
				return true;
			}
		}
		return super.hasPendingEvents(event);
	}

	@Override
	public void open() {
		Logger.log("Panel " + getComponentID() + " open called");
		super.open();
		for (GUIComponent component : components) {
			component.open();
		}
	}

	@Override
	public void close() {
		Logger.log("Panel " + getComponentID() + " close called. Events: \n" + actions[ON_CLOSE]);
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
		for (GUIComponent component : components) {
			component.updateEvents();
			if (!component.isActive() || component.isLocked() || isLocked()) continue;
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