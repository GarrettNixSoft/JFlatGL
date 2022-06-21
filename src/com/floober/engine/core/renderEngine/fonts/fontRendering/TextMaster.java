package com.floober.engine.core.renderEngine.fonts.fontRendering;

import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.TextMeshData;
import com.floober.engine.core.renderEngine.models.ModelLoader;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;

import java.util.*;

public class TextMaster {

	private static Map<FontType, List<GUIText>>[] texts;
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
		renderer.render(texts[layer]);
	}

	public static void loadText(GUIText text) {
		if (!text.isProcessed()) processText(text);
		int layer = (int) text.getPosition().z();
		FontType fontType = text.getFont();
		List<GUIText> textBatch = texts[layer].computeIfAbsent(fontType, k -> new ArrayList<>());
		textBatch.add(text);
	}

	public static void processText(GUIText text) {
//		Logger.log("Processing text: " + text.getTextString());
		FontType fontType = text.getFont();
		TextMeshData data = fontType.loadText(text);
		int vao = ModelLoader.loadToVAO(data.vertexPositions(), data.textureCoords(), text);
		textVAOs.add(vao);
		text.setMeshInfo(vao, data.vertexPositions().length / 3); // there are (length / 3) vertices, since each vertex is 3 floats (x,y,z)
		text.setProcessed(true);
	}

	public static void removeText(GUIText text) {
		text.delete();
		int layer = (int) text.getPosition().z();
		List<GUIText> textBatch = texts[layer].get(text.getFont());
		if (textBatch != null) {
			textBatch.remove(text);
			if (textBatch.isEmpty()) {
				texts[layer].remove(text.getFont());
			}
		}
		text.setProcessed(false);
	}

	public static void removeText(GUIText text, FontType oldFont) {
		text.delete();
		int layer = (int) text.getPosition().z();
		List<GUIText> textBatch = texts[layer].get(oldFont);
		if (textBatch != null) {
			textBatch.remove(text);
			if (textBatch.isEmpty()) {
				texts[layer].remove(oldFont);
			}
		}
	}

	public static void updateTexts(int layer) {
		Set<FontType> keySet = texts[layer].keySet();
		FontType[] fonts = keySet.toArray(new FontType[] {});
		int setSize = keySet.size();
		for (int j = 0; j < setSize; j++) {
			if (keySet.size() != setSize) Logger.log("Key set was size " + setSize + ", but on iteration " + j + " of the loop it is now " + keySet.size());
			FontType fontType = fonts[j];
			List<GUIText> textList = texts[layer].get(fontType);
			int size = textList.size();
			//noinspection ForLoopReplaceableByForEach
			for (int k = 0; k < size; ++k) {
				textList.get(k).update();
			}
		}
	}

	/**
	 * Clear all GUIText objects from the screen.
	 */
	public static void clear() {
		for (Map<FontType, List<GUIText>> map : texts) {
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
		renderer.cleanUp();
	}

}