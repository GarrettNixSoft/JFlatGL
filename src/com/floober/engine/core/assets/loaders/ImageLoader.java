package com.floober.engine.core.assets.loaders;

import com.floober.engine.core.assets.loaders.gameassets.temp.RawTextureComponent;
import com.floober.engine.core.renderEngine.textures.RawTextureData;
import com.floober.engine.core.renderEngine.textures.Texture;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.file.FileUtil;
import com.floober.engine.core.util.file.ResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class ImageLoader {

	private static final List<Integer> textures = new ArrayList<>();

	private static final List<RawTexture> rawImages = new ArrayList<>();

	public static void createOpenGLTextures() {
		for (RawTexture rawTexture : rawImages) {
			TextureComponent textureComponent = rawTexture.convertToOpenGLTexture();
			textures.add(textureComponent.texture().id());
			//
		}
	}

	/**
	 * Load a texture from disk, and send it to the GPU.
	 * @param path The path to the texture file.
	 * @return A Texture to reference the loaded image.
	 */
	public static RawTexture loadTexture(String path) {
		// report load
		Logger.logLoad("Loading texture: " + path);
		// load from file
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		ByteBuffer buffer = tryLoad(path, w, h, comp);
		int width = w.get();
		int height = h.get();
		// create the RawTexture and return it
		return new RawTexture(buffer, width, height);
	}

	public static TextureComponent loadTextureConverted(String path) {
		RawTexture rawTexture = loadTexture(path);
		return rawTexture.convertToOpenGLTexture();
	}

	public static TextureComponent loadTexture(ByteBuffer textureData) {
		// report load
//		Logger.logLoad("Loading texture from ByteBuffer...");
		// init OpenGL texture
		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);
		// get image parameters
		MemoryStack stack = MemoryStack.stackPush();
		IntBuffer w = stack.mallocInt(1);
		IntBuffer h = stack.mallocInt(1);
		IntBuffer comp = stack.mallocInt(1);
		// put the data into a texture ByteBuffer
		ByteBuffer buffer = stbi_load_from_memory(textureData, w, h, comp, 4);
		// do the OpenGL stuff and return the Texture
		int width = w.get();
		int height = h.get();
		// set OpenGL texture settings for this texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		// load the texture for OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		// unbind the texture
		glBindTexture(GL_TEXTURE_2D, 0);
		// add to list to clean up
		textures.add(textureID);
		// create the Texture object and return it
		return new TextureComponent(new Texture(textureID, width, height));
	}

	/**
	 * Load a texture, and send it to the GPU. Set the wrapping mode.
	 * @param path The path to the texture file.
	 * @return A Texture to reference the loaded image.
	 */
	public static TextureComponent loadTexture(String path, int wrapMode) {
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
		// check for transparency
		// set OpenGL texture settings for this texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapMode);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapMode);
		// load the texture for OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		// unbind the texture
		glBindTexture(GL_TEXTURE_2D, 0);
		// add to list to clean up
		textures.add(textureID);
		// create the Texture object and return it
		return new TextureComponent(textureID, width, height);
	}

	/**
	 * Load an image raw, without sending it to the GPU.
	 * Used for loading the game icon to send it to GLFW.
	 * @param path The path of the image file to load.
	 * @return A RawTextureData representation of the image.
	 */
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

	public static BufferedImage loadBufferedImage(String path) {
		// report load
		Logger.logLoad("Loading to buffered image: " + path);
		// get input stream
		InputStream inputStream = tryGetInputStream(path);
		// load buffered image
		try {
			return ImageIO.read(inputStream);
		} catch (Exception e) {
			Logger.logError("Failed to read InputStream to BufferedImage.");
			throw new RuntimeException("Failed to read InputStream to BufferedImage.");
		}
	}

	private static ByteBuffer tryLoad(String path, IntBuffer w, IntBuffer h, IntBuffer comp) {
		// declare the input stream
		InputStream in = tryGetInputStream(path);
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
					+ System.lineSeparator() + stbi_failure_reason() + " ... (Path was " + path + ")");
		}

		return result;

	}

	/**
	 * A utility function to help the game to retrieve InputStreams in both
	 * the development environment and when exported. It will attempt to load
	 * from an exported environment first, to preserve performance when the
	 * game is deployed; if that attempt fails, it will assume the game is
	 * being run in development (an IDE) and try that load method.
	 * @param path The path of the file to load.
	 * @return An InputStream associated with the path, or null if both attempts fail.
	 */
	private static InputStream tryGetInputStream(String path) {
		// declare the input stream
		InputStream in = null;
		// open the stream; how this executes depends on whether the game is running in-IDE or as an exported JAR
		try {
			in = ResourceLoader.getResourceAsStream(path); // exported game
		}
		catch (Exception e1) {
			try {
				Logger.logError("Export-load failed, trying in-development load...");
				in = ResourceLoader.getResourceAsStream("res" + FileUtil.SEPARATOR + path); // in development
			} catch (Exception e2) {
				Logger.logError("In-development load failed. Resource could not be found.");
			}
		}
		Logger.logLoad("Load success!");
		return in;
	}

	/**
	 * Get a ByteBuffer representation of the given BufferedImage's data.
	 * @param bufferedImage The image to convert.
	 * @return The image's data in a ByteBuffer.
	 */
	public static ByteBuffer getBufferedImageData(BufferedImage bufferedImage) {
		// get the image as an input stream
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", os); // I AM RETARDED (this was set to "gif" and made PNG arrays with alpha != 1 invisible)
			InputStream in = new ByteArrayInputStream(os.toByteArray());
			// get the bytes from the input stream
			byte[] imageBytes = {};
			try {
				imageBytes = in.readAllBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// be nice
			in.close();

			ByteBuffer imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
			imageBuffer.put(imageBytes);
			imageBuffer.flip();

			return imageBuffer;

		} catch (Exception e) {
			Logger.logError(e.toString());
			throw new RuntimeException("Died trying to convert BufferedImage to ByteBuffer");
		}
	}

	/**
	 * Delete all textures on shutdown.
	 */
	public static void cleanUp() {
		Logger.log("Cleaning up " + textures.size() + " textures");
		for (int texture : textures) {
			glDeleteTextures(texture);
		}
	}

}