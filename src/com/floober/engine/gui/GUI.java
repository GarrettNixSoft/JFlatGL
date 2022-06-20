package com.floober.engine.gui;

import com.floober.engine.core.util.color.Colors;
import com.floober.engine.gui.component.BackgroundComponent;
import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.gui.component.GUILayer;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.data.Stack;

import java.util.HashMap;
import java.util.HashSet;

public class GUI {

	// addressing this GUI
	private final String id;

	// special component: the BG
	private BackgroundComponent background;
	private final BackgroundComponent noBackground;

	// storing layers: all, and active
	private final HashMap<String, GUILayer> layerStore;
	private final Stack<GUILayer> layerStack;

	// component IDs must be unique within a GUI
	private final HashSet<String> componentIDs;

	public GUI(String id) {
		if (GUIManager.GUI_BANK.containsKey(id)) throw new RuntimeException("A GUI with this ID (" + id + ") already exists!");
		else GUIManager.GUI_BANK.put(id, this);
		this.id = id;
		layerStore = new HashMap<>();
		layerStack = new Stack<>();
		componentIDs = new HashSet<>();
		// no background object to avoid using a null
		noBackground = new BackgroundComponent("background_empty", this, Colors.INVISIBLE);
		background = noBackground;
	}

	/**
	 * Get a component by its ID.
	 * @param componentID the ID of the component to fetch
	 * @return the GUIComponent with the matching ID, or null if none exists
	 */
	public GUIComponent getComponentByID(String componentID) {
		// special case: fetching the background
		if (componentID.equalsIgnoreCase("background")) {
			return background;
		}
		// preemptively fail if the ID has not been registered
		if (!componentIDs.contains(componentID)) {
			Logger.logError("No GUI component with ID " + componentID + " found!", Logger.MEDIUM);
			return null;
		}
		for (GUILayer layer : layerStore.values()) {
			for (GUIComponent component : layer.getAllComponents()) {
				if (component.getComponentID().equals(componentID)) return component;
			}
		}
		Logger.logError("(Fallthrough) No GUI component with ID " + componentID + " found!", Logger.MEDIUM);
		return null;
	}

	/**
	 * Get a layer by its ID.
	 * @param componentID the ID of the layer to fetch
	 * @return the GUILayer with the matching ID, or null if none exists
	 */
	public GUILayer getLayerByID(String componentID) {
		for (GUILayer layer : layerStack.getElements()) {
			if (layer.getComponentID().equals(componentID)) return layer;
		}
		return null;
	}

	/**
	 * Register a GUIComponent for this GUI.
	 * @param component the component to add
	 * @return {@code true} if the component's ID is unique and has been registered,
	 * 			{@code false} otherwise.
	 */
	public boolean registerComponent(GUIComponent component) {
		String componentID = component.getComponentID();
		if (componentIDs.contains(componentID)) {
			return false;
		} else {
			componentIDs.add(componentID);
			Logger.logGUIEvent("Registered component: " + componentID);
			return true;
		}
	}

	/**
	 * Remove a component's ID from the set of registered IDs.
	 * First attempts to get the component from this GUI by the
	 * ID given, and throws a {@code RuntimeException} if the component
	 * is found ({@code getComponentByID()} returns a non-null value).
	 * @param component the component to unregister
	 */
	public void unregisterComponent(GUIComponent component) {
		if (getComponentByID(component.getComponentID()) != null) throw new RuntimeException("Cannot unregister a component that still exists in this GUI!");
		componentIDs.remove(component.getComponentID());
	}

	public boolean deleteComponentByID(String componentID) {
		// fail fast if the component doesn't exist
		if (!componentIDs.contains(componentID)) return false;
		// search each layer for the component
		for (GUILayer layer : layerStore.values()) {
			if (layer.deleteComponentByID(componentID)) return true;
		}
		return false;
	}

	/**
	 * Add a layer to the layer store and stack it immediately.
	 * @param layer the layer to add
	 */
	public void addLayer(GUILayer layer) {
		storeLayer(layer);
		stackLayer(layer);
	}

	/**
	 * Add a GUI layer to the layer store.
	 * @param layer the layer to store
	 */
	public void storeLayer(GUILayer layer) {
		layerStore.put(layer.getComponentID(), layer);
	}

	/**
	 * Place a GUILayer onto the layer stack. If the given layer
	 * is not already in the layer stack, it will be added to it.
	 * @param layer the layer to stack
	 */
	public void stackLayer(GUILayer layer) {
		String layerID = layer.getComponentID();
		if (!layerStore.containsKey(layerID)) layerStore.put(layerID, layer);
		stackLayer(layerID);
	}

	/**
	 * Fetch a layer from the layer store by its ID and place
	 * it onto the layer stack. Throws a {@code RuntimeException} if no
	 * layer with the given ID exists, or if the layer is already
	 * on the layer stack.
	 * @param layerID the ID of the layer to stack
	 */
	public void stackLayer(String layerID) {
		if (layerStore.containsKey(layerID)) {
			GUILayer layer = layerStore.get(layerID);
			if (layerStack.getElements().contains(layer)) throw new RuntimeException("Layer " + layerID + " is already on the layer stack!");
			if (!layerStack.isEmpty()) layerStack.peek().lock();
			layerStack.push(layer);
			layer.open();
			Logger.log("Layer stack succeeded");
		}
		else throw new RuntimeException("GUI layer " + layerID + " does not exist!");
	}

	/**
	 * Close the top layer of this GUI. When the top layer
	 * finishes its close operation, it will be automatically
	 * removed and the layer beneath will activate.
	 */
	public void removeTopLayer() {
		layerStack.peek().close();
	}

	/**
	 * Check if this GUI is completely closed.
	 * @return {@code true} if all layers report having completed
	 * 			their close operation.
	 */
	public boolean isClosed() {
		for (GUILayer layer : layerStack.getElements()) {
			if (!layer.isClosed()) return false;
		}
		return true;
	}

	/**
	 * Add or replace a background component to this GUI.
	 * @param background the background to use
	 */
	public void setBackground(BackgroundComponent background) {
		this.background = background;
	}

	/**
	 * Remove this GUI's background component. Sets the active
	 * background component to the invisible one.
	 */
	public void removeBackground() {
		this.background = noBackground;
	}

	/**
	 * Open this GUI. Automatically opens all layers.
	 */
	public void open() {
		Logger.log("Opening GUI");
		for (GUILayer layer : layerStack.getElements()) {
			layer.open();
		}
	}

	/**
	 * Close this GUI. Automatically closes all layers.
	 */
	public void close() {
		for (GUILayer layer : layerStack.getElements()) {
			layer.close();
//			layer.lock();
		}
	}

	/**
	 * Lock all layers of this GUI.
	 */
	public void lock() {
		for (GUILayer layer : layerStack.getElements()) {
			layer.lock();
		}
	}

	/**
	 * Unlock the top layer of this GUI.
	 */
	public void unlock() {
		for (GUILayer layer : layerStack.getElements()) {
			layerStack.peek().unlock();
		}
	}

	public void update() {
//		Logger.log("Updating GUI");
		if (layerStack.isEmpty()) return;
		// remove top layer if it's closed completely
		if (layerStack.peek().isClosed()) {
			Logger.log("Removing layer " + layerStack.peek().getComponentID());
			layerStack.remove();
			if (!layerStack.isEmpty()) layerStack.peek().unlock();
			else return; // return if the last layer was just removed
		}
		// fully update top layer
		GUILayer topLayer = layerStack.peek();
		topLayer.updateEvents();
		topLayer.checkInput();
		topLayer.update();
		// update everything except input for layers below
		for (GUILayer layer : layerStack.getElements()) {
			if (layer != topLayer) {
				layer.updateEvents();
				layer.updateNoInput();
			}
		}
	}

	public void doTransform() {
		if (layerStack.isEmpty()) return;
		layerStack.peek().doTransform();
	}

	public void render() {
		// render layers
		for (GUILayer layer : layerStack.getElements()) {
			layer.render();
		}
	}

	public String getId() {
		return id;
	}

}