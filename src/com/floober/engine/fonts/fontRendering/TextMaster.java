package com.floober.engine.fonts.fontRendering;

import com.floober.engine.fonts.fontMeshCreator.FontType;
import com.floober.engine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.fonts.fontMeshCreator.TextMeshData;
import com.floober.engine.models.ModelLoader;
import com.floober.engine.particles.ParticleTexture;

import javax.imageio.stream.IIOByteBuffer;
import java.util.*;

public class TextMaster {

	private static final Map<FontType, List<GUIText>> texts = new HashMap<>();
	private static FontRenderer renderer;

	public static void init() {
		renderer = new FontRenderer();
	}

	public static void render() {
		updateTexts();
		renderer.render(texts);
	}

	// TEST
	public static int generatedPerFrame = 0;
	// END_TEST

	public static void loadText(GUIText text) {
		FontType fontType = text.getFont();
		TextMeshData data = fontType.loadText(text);
		int vao = ModelLoader.loadToVAO(data.getVertexPositions(), data.getTextureCoords(), text);
		text.setMeshInfo(vao, data.getVertexCount());
		List<GUIText> textBatch = texts.computeIfAbsent(fontType, k -> new ArrayList<>());
		textBatch.add(text);
		generatedPerFrame++;
	}

	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}

	private static void updateTexts() {
		for (FontType fontType : texts.keySet()) {
			List<GUIText> textList = texts.get(fontType);
			int size = textList.size();
			for (int i = 0; i < size; ++i) {
				textList.get(i).update();
			}
		}
	}

	public static void cleanUp() {
		renderer.cleanUp();
	}

}