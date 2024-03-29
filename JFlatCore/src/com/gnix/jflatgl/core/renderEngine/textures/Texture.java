package com.gnix.jflatgl.core.renderEngine.textures;

public record Texture(int id, int width, int height) {

	public Texture copy() {
		return new Texture(id(), width(), height());
	}

}
