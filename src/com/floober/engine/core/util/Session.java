package com.floober.engine.core.util;

/*
	Store data about the current game session.
	Track things like whether the menu intro
	has been shown, etc.
 */
public class Session {

	private boolean introShown;

	public void setIntroShown(boolean introShown) {
		this.introShown = introShown;
	}

	public boolean isIntroShown() {
		return introShown;
	}

}
