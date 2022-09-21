package com.gnix.jflatgl.gui.event;

import com.gnix.jflatgl.event.QueuedEvent;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.component.GUIComponent;

public abstract class GUIEvent extends QueuedEvent {

	private final GUI gui;

	protected final GUIComponent sourceComponent;
	protected final GUIComponent targetComponent;

	/**
	 * Create a GUIEvent that is not tied to any
	 * specific components.
	 */
	public GUIEvent(boolean blocking) {
		super(blocking);
		this.gui = null;
		sourceComponent = null;
		targetComponent = null;
	}

	/**
	 * Create a GUIEvent that is not tied to any
	 * specific components.
	 */
	public GUIEvent(GUI gui, boolean blocking) {
		super(blocking);
		this.gui = gui;
		sourceComponent = null;
		targetComponent = null;
	}

	/**
	 * Create a GUIEvent targeted at a specific
	 * component, but with no specified origin.
	 * @param targetComponent the component to target
	 */
	public GUIEvent(boolean blocking, GUIComponent targetComponent) {
		super(blocking);
		this.gui = targetComponent.getParent();
		this.sourceComponent = null;
		this.targetComponent = targetComponent;
	}

	/**
	 * Create a GUIEvent targeted at a specific
	 * component and originating from a specific
	 * component.
	 * @param sourceComponent the source component
	 * @param targetComponent the target component
	 */
	public GUIEvent(boolean blocking, GUIComponent sourceComponent, GUIComponent targetComponent) {
		super(blocking);
		this.gui = targetComponent.getParent();
		this.sourceComponent = sourceComponent;
		this.targetComponent = targetComponent;
	}

	public GUI getGUI() {
		return gui;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
