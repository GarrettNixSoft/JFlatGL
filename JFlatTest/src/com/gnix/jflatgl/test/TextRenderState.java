package com.gnix.jflatgl.test;

import com.gnix.jflatgl.core.gameState.GameState;
import com.gnix.jflatgl.core.gameState.GameStateManager;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;

public class TextRenderState extends GameState {

	private GUIText text;

	public TextRenderState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		text = new GUIText();
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update() {

	}

	@Override
	public void render() {

	}

	@Override
	public void cleanUp() {

	}
}
