package com.gnix.jflatgl.gui;

import com.gnix.jflatgl.extension.EngineExtension;

public class GUIExtension extends EngineExtension {

	public GUIExtension() {
		super(GUIManager::update, GUIManager::render);
	}

}
