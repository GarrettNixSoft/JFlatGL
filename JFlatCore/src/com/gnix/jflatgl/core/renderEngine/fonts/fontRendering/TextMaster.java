package com.gnix.jflatgl.core.renderEngine.fonts.fontRendering;

import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.TextMeshData;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.util.Layers;
import com.gnix.jflatgl.core.util.Logger;

import java.util.*;

public class TextMaster {

	private static Map<FontType, Set<GUIText>>[] texts;
	private static FontRenderer renderer;

	public static final List<Integer> textVAOs = new ArrayList<>();

	@SuppressWarnings("unchecked") // suck it, compiler; this is valid
	public static void init() {
		renderer = new FontRenderer();
		texts = new HashMap[Layers.NUM_LAYERS];
		for (int i = 0; i < Layers.NUM_LAYERS; i++) {
			texts[i] = new HashMap<>();
		}
	}

	public static void render(int layer) {
		updateTexts(layer);
//		Logger.log("Rendering layer " + layer);
		renderer.render(texts[layer]);
	}

	public static void loadText(GUIText text) {
		if (!text.isProcessed()) processText(text);
		int layer = text.getLayer();
		FontType fontType = text.getFont();
		Set<GUIText> textBatch = texts[layer].computeIfAbsent(fontType, k -> new HashSet<>());
		textBatch.add(text);
//		Logger.log("Added to layer " + layer + ": " + text.getTextString());
//		Logger.log("Layer now has " + texts[layer].keySet().size() + " font lists in it");
	}

	public static void processText(GUIText text) {
//		Logger.log("Processing text: " + text.getTextString());
		FontType fontType = text.getFont();
		TextMeshData data = fontType.loadText(text);
		int vao = ModelLoader.loadToVAO(data.vertexPositions(), data.textureCoords(), text);
		if (textVAOs.contains(vao)) {
			if (findText(vao)) throw new RuntimeException();
			else textVAOs.remove((Integer) vao);
		}
		textVAOs.add(vao);
//		Logger.log("Text got VAO: " + vao);
		text.setMeshInfo(vao, data.vertexPositions().length / 3); // there are (length / 3) vertices, since each vertex is 3 floats (x,y,z)
		text.setProcessed(true);
		// TEST
//		System.out.println("Printing stack trace:");
//		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
//		for (int i = 1; i < elements.length; i++) {
//			StackTraceElement s = elements[i];
//			System.out.println("\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber() + ")");
//		}
		// END_TEST
	}

	public static void removeText(GUIText text) {
//		Logger.log("REMOVING: " + text.getTextString());
		text.delete();
		textVAOs.remove((Integer) text.getVAO());
		int layer = text.getLayer();
		Set<GUIText> textBatch = texts[layer].get(text.getFont());
		if (textBatch != null) {
			textBatch.remove(text);
			if (textBatch.isEmpty()) {
				texts[layer].remove(text.getFont());
			}
		}
		text.setProcessed(false);
	}

	public static void removeText(GUIText text, FontType oldFont) {
//		Logger.log("REMOVING: " + text.getTextString());
		text.delete();
		textVAOs.remove(text.getVAO());
		int layer = text.getLayer();
		Set<GUIText> textBatch = texts[layer].get(oldFont);
		if (textBatch != null) {
			textBatch.remove(text);
			if (textBatch.isEmpty()) {
				texts[layer].remove(oldFont);
			}
		}
	}

	public static void moveLayers(GUIText text, int oldLayer, int newLayer) {
		// remove from old layer
		Set<GUIText> targetList = texts[oldLayer].get(text.getFont());
		if (targetList != null) {
			texts[oldLayer].get(text.getFont()).remove(text);
			if (targetList.isEmpty()) {
				texts[oldLayer].remove(text.getFont());
			}
		}
		// add to new layer
		Set<GUIText> textBatch = texts[newLayer].computeIfAbsent(text.getFont(), k -> new HashSet<>());
		textBatch.add(text);
	}

	public static void updateTexts(int layer) {
		Set<FontType> keySet = texts[layer].keySet();
//		Logger.log("Updating layer " + layer + "; it has " + keySet.size() + " text lists");
		FontType[] fonts = keySet.toArray(new FontType[] {});
		int setSize = keySet.size();
		for (int j = 0; j < setSize; j++) {
			if (keySet.size() != setSize) Logger.log("Key set was size " + setSize + ", but on iteration " + j + " of the loop it is now " + keySet.size());
			FontType fontType = fonts[j];
			Set<GUIText> textSet = texts[layer].get(fontType);
			GUIText[] textArray = textSet.toArray(new GUIText[0]);
			int size = textSet.size();
			for (int k = 0; k < size; ++k) {
				textArray[k].update();
			}
		}
//		Logger.log("Done updating layer " + layer + "; it has " + keySet.size() + " text lists");
	}

	/**
	 * Clear all GUIText objects from the screen.
	 */
	public static void clear() {
		// this can happen if the splash screen never uses text
		if (texts == null) return;
		for (Map<FontType, Set<GUIText>> map : texts) {
			// delete every GUIText's data
			for (FontType fontType : map.keySet()) {
				for (GUIText guiText : map.get(fontType)) {
					guiText.delete();
				}
			}
			// clear the map, let GC handle all the GUITexts
			map.clear();
		}
	}

	public static void cleanUp() {
		clear();
		renderer.cleanUp();
	}




	private static boolean findText(int vao) {
		boolean found = false;
		for (Map<FontType, Set<GUIText>> map : texts) {
			// delete every GUIText's data
			for (FontType fontType : map.keySet()) {
				for (GUIText guiText : map.get(fontType)) {
					if (guiText.getVAO() == vao) {
						Logger.logError(Logger.HIGH, "Found duplicate VAO for text: " + guiText.getTextString());
						found = true;
						break;
					}
				}
				if (found) break;
			}
			if (found) break;
		}
		return found;
	}

}
