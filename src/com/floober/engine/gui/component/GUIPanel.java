package com.floober.engine.gui.component;

import com.floober.engine.display.Display;
import com.floober.engine.util.Collisions;
import com.floober.engine.util.data.Stack;
import com.floober.engine.util.exception.GUIException;

import java.util.HashMap;

public class GUIPanel extends GUIComponent {

	private final HashMap<String, GUILayer> layerStore;
	private final Stack<GUILayer> layerStack;

	public GUIPanel(String componentID) {
		super(componentID);
		layerStore = new HashMap<>();
		layerStack = new Stack<>();
		setPosition(Display.center(), 0); // default center
		setSize(Display.dimensions()); // default full screen
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
			layerStack.push(layerStore.get(layerID));
			layerStore.get(layerID).open();
		}
	}

	public void removeTopLayer() {
		layerStack.peek().close();
	}

	public void addComponent(GUIComponent component) throws GUIException {
		if (layerStack.size() == 1 && Collisions.contains(getCollisionBox(), component.getCollisionBox())) { // bottom layer must be in bounds
			layerStack.peek().addComponent(component);
		}
		else throw new GUIException("Component is outside of panel bounds: panel " + getComponentID()
				+ " with bounds " + getCollisionBox() + " does not fully contain component " + component.getComponentID()
				+ " with bounds " + component.getCollisionBox());
	}

	// isReady() needn't be overridden

	@Override
	public boolean isClosed() {
		if (super.isClosed()) {
			for (GUILayer layer : layerStack.getElements()) {
				if (!layer.isClosed()) return false;
			}
			return true;
		}
		else return false;
	}

	@Override
	public void open() {
		super.open();
		for (GUILayer layer : layerStack.getElements()) {
			layer.open();
		}
	}

	@Override
	public void close() {
		super.close();
		for (GUILayer layer : layerStack.getElements()) {
			layer.close();
		}
	}

	@Override
	public void update() {
		// remove top layer if it's closed completely
		if (layerStack.peek().isClosed()) {
			layerStack.remove();
			if (layerStack.isEmpty()) {
				// do onClose here
			}
		}
		// update sub-components
		GUILayer topLayer = layerStack.peek();
		topLayer.updateEvents();
		topLayer.checkInput();
		topLayer.update();
	}

	@Override
	public void doTransform() {
		layerStack.peek().doTransform();
	}

	@Override
	public void render() {
		// render sub-components
		for (GUILayer layer : layerStack.getElements()) {
			layer.render();
		}
	}
}