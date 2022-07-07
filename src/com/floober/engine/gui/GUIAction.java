package com.floober.engine.gui;

public interface GUIAction {

	GUIAction NOP = () -> {};

	void onTrigger();

}