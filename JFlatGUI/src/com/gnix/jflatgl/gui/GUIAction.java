package com.gnix.jflatgl.gui;

public interface GUIAction {

	GUIAction NOP = () -> {};

	void onTrigger();

}
