package com.floober.engine.gui.dialogue;

/**
 * @author Floober
 *
 * A DialogueLabel is a line in the dialogue file with no content
 * that marks a location for a DialogueJump to advance to.
 */
public class DialogueLabel extends DialogueLine {

	private final String label;

	public DialogueLabel(String label) {
		super(null, null, null);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
