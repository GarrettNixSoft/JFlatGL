package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.renderEngine.textures.Texture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public record RawTexture(ByteBuffer imageData, int format, int width, int height) {

	public TextureComponent convertToOpenGLTexture() {

//		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

		// init OpenGL texture
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		// load the texture for OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageData);

		// set OpenGL texture settings for this texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//		if (glfwGetCurrentContext() != DisplayManager.primaryWindowID) Logger.log("LOADING TEXTURES ON THE WRONG CONTEXT!");
		// unbind the texture
		glBindTexture(GL_TEXTURE_2D, 0);
		// create the Texture object
		Texture texture = new Texture(textureID, width, height);
		// wrap it in a TextureComponent and return it
		return new TextureComponent(texture);

	}

}
