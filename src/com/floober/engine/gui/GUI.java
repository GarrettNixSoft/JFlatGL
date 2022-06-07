package com.floober.engine.gui;

import com.floober.engine.gui.component.GUIComponent;
import com.floober.engine.gui.component.GUILayer;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.data.Stack;

import java.util.HashMap;
import java.util.HashSet;

public class GUI {

	// addressing this GUI
	private final String id;

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
	}

	/**
	 * Get a component by its ID.
	 * @param componentID the ID of the component to fetch
	 * @return the GUIComponent with the matching ID, or null if none exists
	 */
	public GUIComponent getComponentByID(String componentID) {
		// preemptively fail if the ID has not been registered
		if (!componentIDs.contains(componentID)) return null;
		for (GUILayer layer : layerStack.getElements()) {
			for (GUIComponent component : layer.getAllComponents()) {
				if (component.getComponentID().equals(componentID)) return component;
			}
		}
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
			return true;
		}
	}

	public void addLayer(GUILayer layer) {
		storeLayer(layer);
		layerStack.push(layer);
	}

	public void storeLayer(GUILayer layer) {
		layerStore.put(layer.getComponentID(), layer);
	}

	public void stackLayer(GUILayer layer) {
		layerStack.push(layer);
		layer.open();
	}

	public void stackLayer(String layerID) {
		if (layerStore.containsKey(layerID)) {
			if (!layerStack.isEmpty()) layerStack.peek().lock();
			layerStack.push(layerStore.get(layerID));
			layerStore.get(layerID).open();
			Logger.log("Layer stack succeeded");
		}
	}

	public void removeTopLayer() {
		layerStack.peek().close();
	}

	public boolean isClosed() {
		for (GUILayer layer : layerStack.getElements()) {
			if (!layer.isClosed()) return false;
		}
		return true;
	}

	public void open() {
		Logger.log("Opening GUI");
		for (GUILayer layer : layerStack.getElements()) {
			layer.open();
		}
	}

	public void close() {
		for (GUILayer layer : layerStack.getElements()) {
			layer.close();
//			layer.lock();
		}
	}

	public void lock() {
		for (GUILayer layer : layerStack.getElements()) {
			layer.lock();
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
		// update sub-components
		GUILayer topLayer = layerStack.peek();
		topLayer.updateEvents();
		topLayer.checkInput();
		topLayer.update();
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