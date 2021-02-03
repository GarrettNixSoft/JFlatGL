package com.floober.engine.gui;

import com.floober.engine.gui.component.GUILayer;
import com.floober.engine.gui.component.GUIPanel;
import com.floober.engine.util.Logger;
import com.floober.engine.util.data.Stack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUI {

	// addressing this GUI
	private final String id;

	// storing layers: all, and active
	private final HashMap<String, GUILayer> layerStore;
	private final Stack<GUILayer> layerStack;

	public GUI(String id) {
		this.id = id;
		layerStore = new HashMap<>();
		layerStack = new Stack<>();
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

	public void update() {
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