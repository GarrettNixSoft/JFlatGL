package com.gnix.jflatgl.core.renderEngine.textures;

import java.nio.ByteBuffer;

public class RawTextureData {

	public int width;
	public int height;
	public ByteBuffer buffer;

	public RawTextureData(int width, int height, ByteBuffer buffer) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
	}

}
