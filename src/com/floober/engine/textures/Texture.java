package com.floober.engine.textures;

import org.joml.Vector4f;

public class Texture {
	
	protected final int id;
	protected final int width;
	protected final int height;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
