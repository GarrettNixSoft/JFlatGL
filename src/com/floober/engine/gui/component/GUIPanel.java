package com.floober.engine.gui.component;

import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.event.ClosedEvent;
import com.floober.engine.core.util.Logger;
import com.floober.engine.gui.event.MouseClickEvent;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class GUIPanel extends GUIComponent {

	private final List<GUIComponent> components = new ArrayList<>();
	private RectElement baseElement;

	public GUIPanel(String componentID, GUI parent) {
		super(componentID, parent);
		baseElement = new RectElement(Colors.INVISIBLE, new Vector4f(), getLayer(), true);
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

	public List<GUIComponent> getComponents() {
		return components;
	}

	public void addComponent(GUIComponent component) {
		if (component != this)
			components.add(component);
	}

	@Override
	public void setOpacity(float opacity) {
		for (GUIComponent component : components) {
			component.setOpacity(opacity);
		}
	}

	@Override
	public boolean isClosed() {
		if (components.isEmpty()) {
//			Logger.log("Panel " + getComponentID() + " not closed; empty panel");
			return false;
		}
		for (GUIComponent component : components) {
			if (!component.isClosed()) {
//				Logger.log("Panel " + getComponentID() + " not closed; component " + component.getComponentID() + " is still open");
				return false;
			}
		}
// 		Logger.log("Panel closed!");
		if (hasPendingEvents()) {
//			Logger.log("Panel " + getComponentID() + " not closed; has pending events");
			return false;
		}

		return true;
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
		Logger.log("Panel " + getComponentID() + " close called.");
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
		baseElement.setSize(getScaledSize());
		baseElement.transform();
		for (GUIComponent component : components) {
			component.doTransform();
		}
	}

	@Override
	public void render() {
		baseElement.render();
		for (GUIComponent component : components) {
			component.render();
		}
	}

	@Override
	public void remove() {
		for (GUIComponent component : components) {
			Logger.logGUIEvent("REMOVING COMPONENT: " + component.getComponentID());
			component.remove();
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
		return "GUIPanel[\"" + getComponentID() + "\"]";
	}
}