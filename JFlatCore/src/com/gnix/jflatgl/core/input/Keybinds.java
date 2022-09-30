package com.gnix.jflatgl.core.input;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.data.Pair;

import java.util.HashMap;

/**
 * The Keybinds class should be used to implement the ability for
 * keybinds to be remapped.
 */
public class Keybinds {

	public enum INPUT {
		MOVE_LEFT, MOVE_RIGHT, ATTACK
	}

	public static final HashMap<INPUT, Integer> keybinds = new HashMap<>();

	public static void init() {
		keybinds.put(INPUT.MOVE_LEFT, KeyInput.A);
		keybinds.put(INPUT.MOVE_RIGHT, KeyInput.D);
	}

	public static int getBind(INPUT input) {
		return keybinds.get(input);
	}

	/**
	 * Attempt to bind a key input to an action.
	 * @param input the desired input action to bind a key to
	 * @param key the ID of the key to bind (obtained from the {@code KeyInput} class)
	 * @return a {@code Pair} containing a {@code boolean} and a {@code String}.
	 * 			The boolean value represents whether the key was successfully bound.
	 * 			The String provides a reason for failure in the event that binding
	 * 			fails.
	 * 			<br>
	 * 			A bind can fail if the key to bind is already bound to another action,
	 * 			or if the key is already bound to the same action. If the key is already bound
	 * 			to the same action the bind will actually be considered to succeed, but a
	 * 			warning String will also be returned.
	 * 			In the event of such a collision, the user may be prompted with a warning
	 * 			and asked if they wish to perform this bind anyway. If the user does
	 * 			wish to continue, use the {@code forceBind()} method.
	 */
	public static Pair<Boolean, String> setKeybind(INPUT input, int key) {
		// first, check if this key is already bound
		for (INPUT i : keybinds.keySet()) {
			if (keybinds.get(i) == key) {
				if (i == input) return new Pair<>(true, "That key is already bound to that action.");
				else return new Pair<>(false, "This key is already bound to: " + i);
			}
		}
		// if not, bind it
		keybinds.put(input, key);
		return new Pair<>(true, "");
	}

	/**
	 * Force a keybind when there is already an action to which the requested key is bound.
	 * @param input the new action to bind the key to
	 * @param key the ID of the key to bind (obtained from the {@code KeyInput} class)
	 * @return The input that the key was previously bound to, as that input will be unbound.
	 * 			<br>
	 * 			It is considered an error for this method to be called given a key which is not
	 * 			already bound to a different action.
	 */
	public static INPUT forceBind(INPUT input, int key) {
		// first, check if this key is already bound
		INPUT prevSetting = null;
		for (INPUT i : keybinds.keySet()) {
			if (keybinds.get(i) == key) {
				prevSetting = i;
				break;
			}
		}
		// if there was one, unbind it, force the new bind and return the input that is now not bound
		if (prevSetting != null) {
			keybinds.remove(prevSetting);
			keybinds.put(input, key);
			return prevSetting;
		}
		// otherwise, calling this was an error
		else {
			Logger.logError(Logger.LOW, "Attempted to force a keybind for a non-colliding change!");
			return null;
		}
	}

	/**
	 * Reset keybinds to their default values.
	 */
	public static void setDefaultBinds() {
		// TODO: implement on a per-project basis
	}

}
