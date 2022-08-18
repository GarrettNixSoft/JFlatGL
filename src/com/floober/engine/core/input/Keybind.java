package com.floober.engine.core.input;

import com.floober.engine.core.util.Logger;
import org.json.JSONObject;

import java.util.HashSet;

/**
 * A Keybind represents the association between a named control and up to three keys and one gamepad input. Only one
 * Keybind object may exist per control, per context. This is enforced using an internal HashSet tracking all context
 * and control names used to create Keybinds.
 *
 * @author Floober
 */
public class Keybind {

	// enforce control name uniqueness
	private final static HashSet<String> controls = new HashSet<>();

	// fields for this keybind
	private final String controlName;
	private boolean repeatable;
	private String primaryBind = "none";
	private String secondaryBind = "none";
	private String tertiaryBind = "none";
	private String gamepadBind = "none";
	private boolean varying = false;

	/**
	 * Create a new Keybind associated with the given control.
	 * The control name must be unique (i.e., no other Keybind
	 * object is associated with the same control name).
	 * @param controlName the name of the control to associate with this Keybind
	 */
	public Keybind(String contextName, String controlName) {
		// verify uniqueness
		String contextControlName = contextName + ":" + contextName;
		if (controls.contains(contextControlName)) throw new IllegalArgumentException("Duplicate keybind for " + contextControlName);
		controls.add(contextControlName);
		// assign fields
		this.controlName = controlName;
	}

	public Keybind(String contextName, String controlName, JSONObject json) {
		// verify uniqueness
		String contextControlName = contextName + ":" + controlName;
		if (controls.contains(contextControlName)) throw new IllegalArgumentException("Duplicate keybind for " + contextControlName);
		controls.add(contextControlName);
		// assign fields
		this.controlName = controlName;
		// parse data
		parseJSON(json);
	}

	private void parseJSON(JSONObject json) {
		// whether this input is something you hold down
		repeatable = json.getBoolean("repeatable");
		// get key bindings
		JSONObject keys = json.getJSONObject("keys");
		primaryBind = keys.getString("primary");
		secondaryBind = keys.getString("secondary");
		tertiaryBind = keys.getString("tertiary");
		// get gamepad binding
		gamepadBind = json.getString("gamepad");
	}

	public JSONObject getJSON() {
		// create an empty JSON object
		JSONObject result = new JSONObject();
		// add each binding
		result.put("repeatable", repeatable);
		JSONObject keys = new JSONObject();
		keys.put("primary", primaryBind);
		keys.put("secondary", secondaryBind);
		keys.put("tertiary", tertiaryBind);
		result.put("keys", keys);
		result.put("gamepad", gamepadBind);
		result.put("varying", varying);
		// TODO: gamepad is an obj, with two fields: button, and varying (boolean)
		// return when finished
		return result;
	}

	/**
	 * @return the name of the control associated with this Keybind.
	 */
	public String getControlName() {
		return controlName;
	}

	/**
	 * @return the name of the key assigned as the primary bind for the associated control
	 */
	public String getPrimaryBind() {
		return primaryBind;
	}

	/**
	 * @return the name of the key assigned as the secondary bind for the associated control
	 */
	public String getSecondaryBind() {
		return secondaryBind;
	}

	/**
	 * @return the name of the key assigned as the tertiary bind for the associated control
	 */
	public String getTertiaryBind() {
		return tertiaryBind;
	}

	/**
	 * @return the name of the gamepad input assigned as the gamepad bind for the associated control
	 */
	public String getGamepadBind() {
		return gamepadBind;
	}

	/**
	 * @return whether the gamepad binding for the associated control should vary in intensity
	 */
	public boolean isVarying() {
		return varying;
	}

	/**
	 * @param primaryBind the name of the key assigned as the primary bind for the associated control
	 */
	public void setPrimaryBind(String primaryBind) {
		this.primaryBind = primaryBind;
	}

	/**
	 * @param secondaryBind the name of the key assigned as the secondary bind for the associated control
	 */
	public void setSecondaryBind(String secondaryBind) {
		this.secondaryBind = secondaryBind;
	}

	/**
	 * @param tertiaryBind the name of the key assigned as the tertiary bind for the associated control
	 */
	public void setTertiaryBind(String tertiaryBind) {
		this.tertiaryBind = tertiaryBind;
	}

	/**
	 * @param gamepadBind the name of the gamepad input assigned as the gamepad bind for the associated control
	 */
	public void setGamepadBind(String gamepadBind) {
		this.gamepadBind = gamepadBind;
	}

	/**
	 * @param varying whether the gamepad input should vary in intensity
	 */
	public void setVarying(boolean varying) {
		this.varying = varying;
	}

	// ******************************** CHECKING INPUT ********************************
	public boolean inputPresent(int player) {

		// check the current input mode
		Controls.InputMode inputMode = Controls.getCurrentInputMode();

		// if we're in gamepad mode, check the gamepad input
		if (inputMode == Controls.InputMode.GAMEPAD) {

			// only check the input if it's a valid bind
			if (GamepadNames.GAMEPAD_VALUES.containsKey(gamepadBind)) {

				// check what gamepad input this bind corresponds to
				int gamepadValue = GamepadNames.GAMEPAD_VALUES.get(gamepadBind);

				// if it corresponds to an axis,
				if (Gamepad.isAxis(gamepadBind)) {
					// then if this input is repeatable,
					if (repeatable) {
						// just return whether this axis is not considered to be at rest
						return GamepadInput.getAxis(player, gamepadValue) != GamepadInput.getRestForAxis(gamepadValue);
					}
					// otherwise if it isn't repeatable,
					else {
						// check whether there is a new input for this axis on this frame
						return GamepadInput.axisNewInput(player, gamepadValue);
					}
				}
				// otherwise if it's a specific direction on an axis,
				else if (Gamepad.isDirectional(gamepadBind)) {
					// then if this input is repeatable,
					if (repeatable) {
						// just return whether this axis is currently moved in the given direction
						return GamepadInput.getAxisDirectional(player, gamepadBind);
					}
					// otherwise if it isn't repeatable,
					else {
						// check whether this axis is currently moved in the given direction, and it started moving on this frame
						return	GamepadInput.axisNewInput(player, gamepadValue) &&
								GamepadInput.getAxisDirectional(player, gamepadBind);
					}
				}
				// otherwise if it corresponds to a button,
				else if (Gamepad.isButton(gamepadBind)) {
					// then if this input is repeatable,
					if (repeatable) {
						// just return whether the button is down or not
						return GamepadInput.isHeld(player, gamepadValue);
					}
					// otherwise if it isn't repeatable,
					else {
						// check whether the button was newly pressed on this frame
						return GamepadInput.isPressed(player, gamepadValue);
					}
				}
				else return false;

			}
			// if it isn't a valid bind, this input cannot be present
			else return false;

		}
		// otherwise, if we're in MKB mode,
		else if (inputMode == Controls.InputMode.KEYBOARD_MOUSE) {

			// try primary, secondary, then tertiary
			if (isKeyboardOrMouseInputPresent(primaryBind)) return true;
			else if (isKeyboardOrMouseInputPresent(secondaryBind)) return true;
			else return isKeyboardOrMouseInputPresent(tertiaryBind);

		}
		// otherwise, this isn't a possible state, the world is ending, but that doesn't matter -- this input just isn't present
		return false;

	}

	private boolean isKeyboardOrMouseInputPresent(String binding) {

		// first, check if this is a key bind
		if (KeyNames.KEY_VALUES.containsKey(binding)) {

			// if it is, get the key value it corresponds to
			int keyValue = KeyNames.KEY_VALUES.get(binding);

			// if this input is repeatable,
			if (repeatable) {
				// return whether this key is down
				return KeyInput.isHeld(keyValue);
			}
			// otherwise if it isn't repeatable,
			else {
				// return whether this key is newly pressed on this frame
				return KeyInput.isPressed(keyValue);
			}

		}
		// otherwise if it isn't a key bind, check if it's a mouse bind
		else if (MouseNames.MOUSE_VALUES.containsKey(binding)) {

			// if it is, get the mouse value it corresponds to
			int mouseValue = MouseNames.MOUSE_VALUES.get(binding);

			// if this input is repeatable,
			if (repeatable) {

				// then if it's an axis input,
				if (MouseInput.isAxis(mouseValue)) {
					// return whether the mouse has moved on this axis on this frame
					return MouseInput.getAxis(mouseValue) != 0;
				}
				// otherwise if it's a button input,
				else if (MouseInput.isButton(mouseValue)) {
					// return whether the button is down
					return MouseInput.isHeld(mouseValue);
				}
				// otherwise, this is not a valid bind
				else return false;

			}
			// otherwise if it isn't repeatable,
			else {

				// then if it's an axis input,
				if (MouseInput.isAxis(mouseValue)) {
					// then return whether the mouse started moving on this axis on this frame
					return MouseInput.newMotionOnAxis(mouseValue);
				}
				// otherwise if it's a button input,
				else if (MouseInput.isButton(mouseValue)) {
					// return whether the button is newly pressed on this frame
					return MouseInput.isPressed(mouseValue);
				}
				// otherwise, this is not a valid bind
				else return false;

			}

		}
		// otherwise, this is not a valid bind
		else return false;

	}

	public static float getVaryingInput(int player) {
		// TODO
		return 0;
	}

}
