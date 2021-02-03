package com.floober.engine.gui.event;

import com.floober.engine.event.QueuedEvent;
import com.floober.engine.gui.component.GUIComponent;

public abstract class GUIEvent extends QueuedEvent {

	protected final GUIComponent sourceComponent;
	protected final GUIComponent targetComponent;

	/**
	 * Create a GUIEvent that is not tied to any
	 * specific components.
	 */
	public GUIEvent(boolean blocking) {
		super(blocking);
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
		this.sourceComponent = sourceComponent;
		this.targetComponent = targetComponent;
	}

	public String toString() {
		return this.getClass().getSimpleName();
	}

}