package com.gnix.jflatgl.core.input;

import java.util.HashMap;
import java.util.Map;

public class ControlContext {

	private final String contextName;
	private final Map<String, Keybind> keybinds;

	public ControlContext(String contextName) {
		this.contextName = contextName;
		this.keybinds = new HashMap<>();
	}

	public void addKeybindForControl(String controlName) {
		Keybind keybind = new Keybind(contextName, controlName);
		keybinds.put(controlName, keybind);
	}

	public void addKeybind(Keybind keybind) {
		keybinds.put(keybind.getControlName(), keybind);
	}

	public Keybind getKeybindForControl(String control) {
		return keybinds.get(control);
	}

	public Map<String, Keybind> getKeybinds() {
		return keybinds;
	}

	public boolean hasControl(String control) {
		return keybinds.containsKey(control);
	}

	public void setKeybind(String controlName, String primaryKey, String secondaryKey, String tertiaryKey, String gamepadBind) {
		// if there is no keybind for this control, create it
		if (!keybinds.containsKey(controlName)) {
			addKeybindForControl(controlName);
		}
		// assign the keybind settings
		Keybind keybind = keybinds.get(controlName);
		keybind.setPrimaryBind(primaryKey);
		keybind.setSecondaryBind(secondaryKey);
		keybind.setTertiaryBind(tertiaryKey);
		keybind.setGamepadBind(gamepadBind);
	}

	public void setPrimaryKey(String controlName, String primaryKey) {
		if (!keybinds.containsKey(controlName)) throw new RuntimeException("No keybind for control " + contextName + ":" + controlName + " exists!");
		keybinds.get(controlName).setPrimaryBind(primaryKey);
	}

	public void setSecondaryKey(String controlName, String secondaryKey) {
		if (!keybinds.containsKey(controlName)) throw new RuntimeException("No keybind for control " + contextName + ":" + controlName + " exists!");
		keybinds.get(controlName).setSecondaryBind(secondaryKey);
	}

	public void setTertiaryKey(String controlName, String tertiaryKey) {
		if (!keybinds.containsKey(controlName)) throw new RuntimeException("No keybind for control " + contextName + ":" + controlName + " exists!");
		keybinds.get(controlName).setTertiaryBind(tertiaryKey);
	}

	public void setGamepadBind(String controlName, String gamepadBind) {
		if (!keybinds.containsKey(controlName)) throw new RuntimeException("No keybind for control " + contextName + ":" + controlName + " exists!");
		keybinds.get(controlName).setGamepadBind(gamepadBind);
	}

}
