package com.floober.engine.renderEngine.textures;

import static org.lwjgl.opengl.GL11.glGenTextures;

public class TextureUtil {

	public static Texture createEmptyTexture(int width, int height) {
		int textureID = glGenTextures();
		return new Texture(textureID, width, height);
	}

}