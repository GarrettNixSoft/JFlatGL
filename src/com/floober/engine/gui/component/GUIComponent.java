package com.floober.engine.gui.component;

import com.floober.engine.event.MultiEventQueue;
import com.floober.engine.gui.GUIAction;
import com.floober.engine.gui.event.ClosedEvent;
import com.floober.engine.gui.event.GUIEvent;
import com.floober.engine.gui.event.ReadyEvent;
import com.floober.engine.util.Logger;
import com.floober.engine.util.input.MouseInput;
import com.floober.engine.util.math.Collisions;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;

public abstract class GUIComponent {

	// addressing this component
	private final String componentID;

	// location on screen
	private final Vector3f position = new Vector3f(), originalPosition = new Vector3f();
	private final Vector2f size = new Vector2f();
	private float scale = 1;

	// appearance
	private final Vector4f primaryColor = new Vector4f(0);
	private final Vector4f secondaryColor = new Vector4f(0);
	private final Vector4f tertiaryColor = new Vector4f(0);
	private float opacity = 1;

	// events to process
	private final MultiEventQueue<GUIEvent> eventQueue = new MultiEventQueue<>();

	// transition configurations
	private final HashMap<String, GUIAction> transitions = new HashMap<>();

	// interactions
	protected final GUIAction[] actions = new GUIAction[8];
	protected static final int ON_OPEN = 0;
	protected static final int ON_OPEN_COMPLETE = 1;
	protected static final int ON_CLOSE = 2;
	protected static final int ON_CLOSE_COMPLETE = 3;
	protected static final int ON_MOUSE_OVER = 4;
	protected static final int ON_MOUSE_EXIT = 5;
	protected static final int ON_LEFT_CLICK = 6;
	protected static final int ON_RIGHT_CLICK = 7;

	// enter/exit
	private boolean active;
	private boolean locked;

	/**
	 * Construct a GUIComponent. All GUIComponents must
	 * have an ID.
	 * @param componentID this component's ID
	 */
	public GUIComponent(String componentID) {
		this.componentID = componentID;
		onOpen(new GUIAction());
		onClose(new GUIAction());
	}

	// CONSTRUCTING GUI ELEMENTS
	public GUIComponent location(Vector3f location) {
		setPosition(location);
		return this;
	}

	public GUIComponent location(Vector2f location, int layer) {
		setPosition(location, layer);
		return this;
	}

	public GUIComponent size(Vector2f size) {
		setSize(size);
		return this;
	}

	public GUIComponent scale(float scale) {
		setScale(scale);
		return this;
	}

	public GUIComponent primaryColor(Vector4f primaryColor) {
		setPrimaryColor(primaryColor);
		return this;
	}

	public GUIComponent secondaryColor(Vector4f secondaryColor) {
		setSecondaryColor(secondaryColor);
		return this;
	}

	public GUIComponent tertiaryColor(Vector4f tertiaryColor) {
		setTertiaryColor(tertiaryColor);
		return this;
	}

	public GUIComponent opacity(float opacity) {
		setOpacity(opacity);
		return this;
	}

	public GUIComponent onOpen(GUIAction action) {
		actions[ON_OPEN] = action.addPerformActionOnTrigger(() -> queueEvent(new ReadyEvent(this)));
		return this;
	}

	public GUIComponent onOpenComplete(GUIAction action) {
		actions[ON_OPEN_COMPLETE] = action;
		return this;
	}

	public GUIComponent onClose(GUIAction action) {
		actions[ON_CLOSE] = action
//				.addPerformActionOnTrigger(() -> Logger.log("This is the inserted event from the GUIComponent class, added on component " + componentID))
				.addPerformActionOnTrigger(() -> queueEvent(new ClosedEvent(this)));
		return this;
	}

	public GUIComponent onCloseComplete(GUIAction action) {
		actions[ON_CLOSE_COMPLETE] = action;
		return this;
	}

	public GUIComponent onMouseOver(GUIAction action) {
		actions[ON_MOUSE_OVER] = action;
		return this;
	}

	public GUIComponent onMouseExit(GUIAction action) {
		actions[ON_MOUSE_EXIT] = action;
		return this;
	}

	public GUIComponent onLeftClick(GUIAction action) {
		actions[ON_LEFT_CLICK] = action;
		return this;
	}

	public GUIComponent onRightClick(GUIAction action) {
		actions[ON_RIGHT_CLICK] = action;
		return this;
	}

	// GETTERS
	public String getComponentID() {
		return componentID;
	}

	public boolean isActive() { return active; }

	public boolean isLocked() {
		return locked;
	}

	public boolean isClosed() {
		return !active && !hasPendingEvents();
	}

	public boolean hasPendingEvents() {
		return !eventQueue.isEmpty();
	}

	/**
	 * Custom hasPendingEvents() method used by the ClosedEvent class, which passes itself.
	 * Returns true if the queue is empty, OR, the only remaining event is the ClosedEvent
	 * that called this method.
	 * @param event the ClosedEvent calling this method
	 * @return {@code true} if the queue is empty, or the calling object is the only event in the queue
	 */
	public boolean hasPendingEvents(ClosedEvent event) {
		if (eventQueue.isEmpty()) return false;
		else {
			if (eventQueue.getRunningEvents().size() == 1 && eventQueue.getRunningEvents().contains(event)) return false;
			else return true;
		}
	}

	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public Vector3f getOriginalPosition() { return new Vector3f(originalPosition); }

	public Vector2f getOffset() {
		Vector3f offset3 = getPosition().sub(getOriginalPosition());
		return new Vector2f(offset3.x, offset3.y);
	}

	public Vector2f getSize() {
		return new Vector2f(size);
	}

	public float getScale() {
		return scale;
	}

	public Vector4f getPrimaryColor() {
		return new Vector4f(primaryColor);
	}

	public Vector4f getSecondaryColor() {
		return new Vector4f(secondaryColor);
	}

	public Vector4f getTertiaryColor() {
		return new Vector4f(tertiaryColor);
	}

	public float getOpacity() {
		return opacity;
	}

	/**
	 * Get a Vector4f representing the top-left corner
	 * of this component as the (x,y) values and the
	 * bottom-left corner as the (z,w) values
	 * @return the collision box
	 */
	public Vector4f getCollisionBox() {
		Vector2f scaledSize = getSize().mul(scale);
		return Collisions.createCollisionBox(position, scaledSize);
	}

	// SETTERS
	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			trigger(ON_OPEN_COMPLETE);
		}
		else {
			trigger(ON_CLOSE_COMPLETE);
		}
	}

	public void lock() {
		locked = true;
		Logger.log("Component " + componentID + " locked");
	}

	public void unlock() {
		locked = false;
		Logger.log("Component " + componentID + " unlocked");
	}

	public void setPosition(Vector3f position) {
		this.position.set(position);
		this.originalPosition.set(position);
	}

	public void setPosition(Vector2f position, float z) {
		this.position.set(position, z);
		this.originalPosition.set(position, z);
	}

	public void setOffsetPosition(Vector2f offsetPosition) {
		this.position.set(getOriginalPosition().add(offsetPosition.x, offsetPosition.y, 0));
	}

	public void offsetPositionBy(Vector2f offset) {
		this.position.add(offset.x, offset.y, 0);
	}

	public void resetPosition() {
		this.position.set(originalPosition);
	}

	public void setSize(Vector2f size) {
		this.size.set(size);
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setPrimaryColor(Vector4f primaryColor) {
		this.primaryColor.set(primaryColor);
	}

	public void setSecondaryColor(Vector4f secondaryColor) {
		this.secondaryColor.set(secondaryColor);
	}

	public void setTertiaryColor(Vector4f tertiaryColor) {
		this.tertiaryColor.set(tertiaryColor);
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	// ACTIONS
	public void queueEvent(GUIEvent event) {
		eventQueue.queueEvent(event);
	}

	public void updateEvents() {
		eventQueue.update();
	}

	public void open() {
		if (isActive()) {
			Logger.log("Open failed; " + componentID + " is already active");
			return;
		}
		trigger(ON_OPEN);
	}

	public void close() {
		if (isClosed()) return;
		Logger.log("Closing component " + componentID);
		trigger(ON_CLOSE);
	}

	public void checkInput() {
		if (mouseOver()) {
			trigger(ON_MOUSE_OVER);
		}
		else if (mouseExit()) {
			trigger(ON_MOUSE_EXIT);
		}
		if (leftClick()) {
			trigger(ON_LEFT_CLICK);
		}
		if (rightClick()) {
			trigger(ON_RIGHT_CLICK);
		}
	}

	private void trigger(int actionID) {
		if (actions[actionID] != null) actions[actionID].trigger();
	}

	// ABSTRACT METHODS

	public abstract void update();
	public abstract void doTransform();
	public abstract void render();

	/**
	 * Check if the mouse has entered this component's
	 * collision box since the previous frame.
	 * @return true if the collision box does not contain
	 * the mouse's previous position but does contain the
	 * mouse's current position, or false otherwise
	 */
	private boolean mouseOver() {
		return !Collisions.contains(getCollisionBox(), MouseInput.getPrevPos()) &&
				Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

	/**
	 * Check if the mouse has exited this component's
	 * collision box since the previous frame.
	 * @return true if the collision box contains the
	 * mouse's previous position but does not contain
	 * the mouse's current position, or false otherwise
	 */
	private boolean mouseExit() {
		return Collisions.contains(getCollisionBox(), MouseInput.getPrevPos()) &&
				!Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

	/**
	 * Check if the user has clicked on this component
	 * with the left mouse button.
	 * @return true if the collision box contains the
	 * mouse's current position and the left mouse button
	 * is clicked
	 */
	private boolean leftClick() {
		return MouseInput.leftClick() && Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

	/**
	 * Check if the user has clicked on this component
	 * with the right mouse button.
	 * @return true if the collision box contains the
	 * mouse's current position and the right mouse button
	 * is clicked
	 */
	private boolean rightClick() {
		return MouseInput.rightClick() && Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

}