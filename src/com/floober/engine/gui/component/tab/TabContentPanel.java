package com.floober.engine.gui.component.tab;

import com.floober.engine.core.util.Logger;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.GUIAction;
import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.gui.component.GUIPanel;
import com.floober.engine.gui.event.BlockingDelayEvent;

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