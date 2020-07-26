package com.floober.engine.loaders;

import com.floober.engine.textures.RawTextureData;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.util.Logger;
import com.floober.engine.util.file.ResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.*;

public class ImageLoader {

	private static final List<Integer> textures = new ArrayList<>();

	public static Texture loadTexture(String path) {
		// report load
		Logger.logLoad("Loading texture: " + path);
		// init OpenGL texture
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		// load from file
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		ByteBuffer buffer = tryLoad(path, w, h, comp);
		int width = w.get();
		int height = h.get();
		// set OpenGL texture settings for this texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		// load the texture for OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		// unbind the texture
		glBindTexture(GL_TEXTURE_2D, 0);
		// add to list to clean up
		textures.add(textureID);
		// create the Texture object and return it
		return new Texture(textureID, width, height);
	}

	public static RawTextureData loadImageRaw(String path) {
		// report load
		Logger.logLoad("Loading raw image: " + path);
		// load from file
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		ByteBuffer buffer = tryLoad(path, w, h, comp);
		int width = w.get();
		int height = h.get();
		return new RawTextureData(width, height, buffer);
	}

	public static void cleanUp() {
		System.out.println("Cleaning up " + textures.size() + " textures");
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
	}

	private static ByteBuffer tryLoad(String path, IntBuffer w, IntBuffer h, IntBuffer comp) {
		// declare the input stream
		InputStream in;
		// open the stream; how this executes depends on whether the game is running in-IDE or as an exported JAR
		try {
			in = ResourceLoader.getResourceAsStream(path); // exported game
		}
		catch (Exception e) {
			Logger.logLoad("Export-load failed, trying in-development load...");
			in = ResourceLoader.getResourceAsStream("res/" + path); // in development
		}
		// get the bytes from the input stream
		byte[] imageBytes = {};
		try {
			imageBytes = in.readAllBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
		imageBuffer.put(imageBytes);
		imageBuffer.flip();

		ByteBuffer result = stbi_load_from_memory(imageBuffer, w, h, comp, 4);

		if (result == null) {
			throw new RuntimeException("Failed to load an image file!"
					+ System.lineSeparator() + stbi_failure_reason());
		}

		return result;

	}
	
}