package com.floober.engine.renderEngine;

import com.floober.engine.display.DisplayManager;
import com.floober.engine.util.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL21.GL_PIXEL_PACK_BUFFER;

public class Screenshot {

	static ByteBuffer buffer;

	static {
		// get maximum display size: 4K
		buffer = BufferUtils.createByteBuffer(3840 * 2160 * 4);
	}

	public static void takeScreenshot(String targetPath) {
		Logger.log("Attempting to take screenshot...");
		// Get pixel data
		// PBO for download
		int PBOHandle = glGenBuffers();
		// Set to front buffer
		GL11.glReadBuffer(GL11.GL_FRONT);
		// Bind the PBO
		glBindBuffer(GL_PIXEL_PACK_BUFFER, PBOHandle);
		// Create the PBO
		glBufferData(GL_PIXEL_PACK_BUFFER, buffer.capacity() * 4L, GL_STREAM_READ);
		// Read to the PBO
		GL11.glReadPixels(0, 0, DisplayManager.getPrimaryGameWindow().getWidth(), DisplayManager.getPrimaryGameWindow().getHeight(), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
		// Get the data
		buffer = glMapBuffer(GL_PIXEL_PACK_BUFFER, GL_READ_WRITE, null);
		// Unmap the buffer and go back to normal
		glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
		glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);
		// done
		// Format it and write it to the file, but do that in another, separate thread
		ScreenshotSaveProcess saveProcess = new ScreenshotSaveProcess(buffer, targetPath);
		saveProcess.start();
	}

	static class ScreenshotSaveProcess extends Thread {

		private final ByteBuffer pixelData;
		private final String targetPath;

		public ScreenshotSaveProcess(ByteBuffer pixelData, String targetPath) {
			this.pixelData = pixelData;
			this.targetPath = targetPath;
		}

		@Override
		public void run() {
			// dimensions and bytes
			int width = DisplayManager.getPrimaryGameWindow().getWidth();
			int height = DisplayManager.getPrimaryGameWindow().getHeight();
			int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
			// location to save
			File file = new File(targetPath); // The file to save to.
			if (!file.exists()) { // If the file doesn't exist (99.999% of the time this is the case)
				if (!file.mkdirs()) { // Then if there's an error creating the directory structure for the screenshot file,
					Logger.log("Failed to create screenshot directory!"); // Complain about it
					return; // and then die
				}
			}
			// perform the save
			String format = "PNG"; // Example: "PNG" or "JPG"
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					int i = (x + (width * y)) * bpp;
					int r = pixelData.get(i) & 0xFF;
					int g = pixelData.get(i + 1) & 0xFF;
					int b = pixelData.get(i + 2) & 0xFF;
					image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
				}
			}
			try {
				ImageIO.write(image, format, file);
			} catch (IOException e) { e.printStackTrace(); }
			Logger.log("Screenshot saved. Location: " + file.getAbsolutePath());
		}

	}

}