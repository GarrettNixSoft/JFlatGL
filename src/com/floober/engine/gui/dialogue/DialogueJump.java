package com.floober.engine.gui.dialogue;

public class DialogueJump extends DialogueLine {

	private final String targetLabel;

	public DialogueJump(String targetLabel) {
		super(null, null, null);
		this.targetLabel = targetLabel;
	}

	public String getTargetLabel() {
		return targetLabel;
	}

}
