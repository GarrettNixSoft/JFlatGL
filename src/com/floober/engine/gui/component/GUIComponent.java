package com.floober.engine.gui.component;

import com.floober.engine.event.MultiEventQueue;
import com.floober.engine.gui.GUI;
import com.floober.engine.gui.GUIAction;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.math.Collisions;
import com.floober.engine.gui.event.ClosedEvent;
import com.floober.engine.gui.event.GUIEvent;
import com.floober.engine.gui.event.MouseClickEvent;
import com.floober.engine.gui.event.ReadyEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.file.Path;
import java.nio.file.attribute.AclEntry;
import java.util.HashMap;

public abstract class GUIComponent {

	// addressing this component
	private final String componentID;
	private final GUI parent;

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
	private final GUIAction[] actions = new GUIAction[8];
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
	 * @param parent the top-level GUI parent of this component
	 */
	public GUIComponent(String componentID, GUI parent) {
		this.componentID = componentID;
		this.parent = parent;
		if (!parent.registerComponent(this)) throw new RuntimeException("Failed to register component id (" + componentID + "), ID is not unique!");
		onOpen(GUIAction.NOP);
		onClose(GUIAction.NOP);
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

	public GUIComponent location(float x, float y, int layer) {
		setPosition(x, y, layer);
		return this;
	}

	public GUIComponent size(Vector2f size) {
		setSize(size);
		return this;
	}

	public GUIComponent size(float width, float height) {
		setSize(width, height);
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
		actions[ON_OPEN] = () -> queueEvent(new ReadyEvent(this));
		return this;
	}

	public GUIComponent onOpenComplete(GUIAction action) {
		actions[ON_OPEN_COMPLETE] = action;
		return this;
	}

	public GUIComponent onClose(GUIAction action) {
		actions[ON_CLOSE] = () -> {
//			Logger.log("This is the inserted event from the GUIComponent class, added on component " + componentID);
			action.onTrigger();
			queueEvent(new ClosedEvent(this));
		};
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
	public GUI getParent() {return parent; }
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
			return eventQueue.getRunningEvents().size() != 1 || !eventQueue.getRunningEvents().contains(event);
		}
	}

	// Position vectors
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	public Vector3f getOriginalPosition() {
		return new Vector3f(originalPosition);
	}

	// Position components
	public float getX() {
		return position.x;
	}
	public float getY() {
		return position.y;
	}
	public int getLayer() {
		return (int) position.z;
	}

	public float getLeft() {
		return getX() - size.x * scale / 2;
	}

	public float getRight() {
		return getX() + size.x * scale / 2;
	}

	public float getTop() {
		return getY() - size.y * scale / 2;
	}

	public float getBottom() {
		return getY() + size.y * scale / 2;
	}

	// Size values
	public Vector2f getSize() {
		return new Vector2f(size);
	}
	public Vector2f getScaledSize() { return new Vector2f(size).mul(scale); }
	public float getWidth() {
		return size.x;
	}
	public float getHeight() {
		return size.y;
	}
	public float getScale() {
		return scale;
	}

	public float getScaledWidth() {
		return size.x * scale;
	}
	public float getScaledHeight() {
		return size.y * scale;
	}

	// Color values
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

	// Actors
	public GUIAction[] getActions() {
		return actions;
	}

	/**
	 * Get this component's current offset as applied by
	 * any events or interactions.
	 * @return 	the difference between this component's current
	 * 			visible position and its original position
	 */
	public Vector2f getOffset() {
		Vector3f offset3 = getPosition().sub(getOriginalPosition());
		return new Vector2f(offset3.x, offset3.y);
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

	public void setPosition(float x, float y, int layer) {
		setPosition(new Vector3f(x, y, layer));
	}

	public void setPosition(Vector2f position, float z) {
		this.position.set(position, z);
		this.originalPosition.set(position, z);
	}

	public void setX(float x) {
		this.position.setComponent(0, x);
	}

	public void setY(float y) {
		this.position.setComponent(1, y);
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

	public void setSize(float width, float height) { this.size.set(width, height); }

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
//		Logger.log("Queued Event: " + event.getClass());
	}

	public void updateEvents() {
//		Logger.log("Update events on " + getComponentID());
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
//		if (leftClick()) {
//			trigger(ON_LEFT_CLICK);
//		}
//		if (rightClick()) {
//			trigger(ON_RIGHT_CLICK);
//		}
	}

	public boolean consumeClick(MouseClickEvent clickEvent) {
		if (clickEvent.button() == MouseInput.LEFT && mouseHover()) {
			trigger(ON_LEFT_CLICK);
			Logger.logGUIInteraction("Component " + componentID + " consumed the Left Click event!");
			return true;
		}
		else if (clickEvent.button() == MouseInput.RIGHT && mouseHover()) {
			trigger(ON_RIGHT_CLICK);
			Logger.logGUIInteraction("Component " + componentID + " consumed the Right Click event!");
			return true;
		}
		else {
			Logger.logGUIInteraction("Component " + componentID + " did not consume the click event.");
			return false;
		}
	}

	private void trigger(int actionID) {
		if (actions[actionID] != null) actions[actionID].onTrigger();
//		if (actionID == ON_LEFT_CLICK) Logger.log("Triggered left click!");
	}


	// ABSTRACT METHODS
	public abstract void update();
	public abstract void doTransform();
	public abstract void render();

	// force implement this in case any component uses a GUIText
	public abstract void remove();

	/**
	 * Check if the mouse has entered this component's
	 * collision box since the previous frame.
	 * @return true if the collision box does not contain
	 * the mouse's previous position but does contain the
	 * mouse's current position, or false otherwise
	 */
	protected boolean mouseOver() {
		return !Collisions.contains(getCollisionBox(), MouseInput.getPrevPos()) && mouseHover();
	}

	/**
	 * Check if the mouse is over this component's
	 * collision box.
	 * @return {@code true} if the mouse is over this component
	 */
	protected boolean mouseHover() {
		return Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

	/**
	 * Check if the mouse has exited this component's
	 * collision box since the previous frame.
	 * @return true if the collision box contains the
	 * mouse's previous position but does not contain
	 * the mouse's current position, or false otherwise
	 */
	protected boolean mouseExit() {
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
	protected boolean leftClick() {
		return MouseInput.leftClick() && Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

	/**
	 * Check if the user has clicked on this component
	 * with the right mouse button.
	 * @return true if the collision box contains the
	 * mouse's current position and the right mouse button
	 * is clicked
	 */
	protected boolean rightClick() {
		return MouseInput.rightClick() && Collisions.contains(getCollisionBox(), MouseInput.getMousePos());
	}

}