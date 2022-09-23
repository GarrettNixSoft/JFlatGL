package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.renderEngine.elements.geometry.OutlineElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.RectElement;
import com.gnix.jflatgl.core.util.color.Colors;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.event.ClosedEvent;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.gui.event.MouseClickEvent;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GUIPanel extends GUIComponent {

	private final List<GUIComponent> components = new ArrayList<>();
	private final RectElement baseElement;
	private final OutlineElement borderElement;

	private float borderWidth;

	public GUIPanel(String componentID, GUI parent) {
		super(componentID, parent);
		baseElement = new RectElement(Colors.INVISIBLE, new Vector4f(), getLayer(), true);
		borderElement = new OutlineElement(Colors.INVISIBLE, new Vector4f(), getLayer(), 0, true);
	}

	protected RectElement getBaseElement() {
		return baseElement;
	}

	protected OutlineElement getBorderElement() {
		return borderElement;
	}

	public GUIPanel borderWidth(float borderWidth) {
		setBorderWidth(borderWidth);
		return this;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
	}

	@Override
	public void setPosition(float x, float y, int layer) {
		super.setPosition(x, y, layer);
	}

	public List<GUIComponent> getComponents() {
		return components;
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
		Logger.logGUIEvent("Panel " + getComponentID() + " open called");
		super.open();
		for (GUIComponent component : components) {
			component.open();
		}
	}

	@Override
	public void close() {
		Logger.logGUIEvent("Panel " + getComponentID() + " close called.");
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
		// background color
		baseElement.setPosition(getPosition());
		baseElement.setSize(getScaledSize());
		baseElement.transform();
		baseElement.setColor(getPrimaryColor().mul(getOpacity()));
		// border color
		borderElement.setPosition(getPosition());
		borderElement.setSize(getScaledSize());
		borderElement.setLineWidth(borderWidth * getScale());
		borderElement.transform();
		borderElement.setColor(getSecondaryColor().mul(getOpacity()));
		// subcomponents
		for (GUIComponent component : components) {
			component.doTransform();
		}
	}

	@Override
	public void render() {
		baseElement.render();
		borderElement.render();
		for (GUIComponent component : components) {
			component.render();
		}
	}

	@Override
	public void remove() {
		for (GUIComponent component : components) {
			Logger.logGUIEvent("REMOVING COMPONENT: " + component.getComponentID());
			component.removeComponent();
		}
	}

	@Override
	public void restore() {
		for (GUIComponent component : components) {
			Logger.logGUIEvent("RESTORING COMPONENT: " + component.getComponentID());
			component.restore();
		}
	}

	@Override
	public String toString() {
		return "GUIPanel[\"" + getComponentID() + "\"]";
	}
}
