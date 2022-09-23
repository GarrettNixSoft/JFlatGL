package com.gnix.jflatgl.gui.component.tab;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.GUIAction;
import com.gnix.jflatgl.gui.component.GUIComponent;
import com.gnix.jflatgl.gui.component.GUIPanel;
import com.gnix.jflatgl.gui.event.BlockingDelayEvent;

public class TabContentPanel extends GUIPanel {

	private final float closeTime;

	public TabContentPanel(String componentID, GUI parent, float closeTime) {
		super(componentID, parent);
		this.closeTime = closeTime;
	}

	/**
	 * Adding a component to a TabContentPanel adds the component
	 * to the panel as usual, but also adds onto its {@code ON_CLOSE} action
	 * a blocking delay event with a duration equal to the parent
	 * TabbedPanel's configured {@code closeTime}.
	 * @param component the component to add
	 */
	@Override
	public void addComponent(GUIComponent component) {
		super.addComponent(component);
		GUIAction oldAction = component.getActions()[ON_CLOSE];
		component.getActions()[ON_CLOSE] = () -> {
			oldAction.onTrigger();
			component.queueEvent(new BlockingDelayEvent(closeTime));
		};
		Logger.log("Component " + component.getComponentID() + " added to tab " + getComponentID() + " and onClose() delay of " + closeTime + "s added");
	}
}
