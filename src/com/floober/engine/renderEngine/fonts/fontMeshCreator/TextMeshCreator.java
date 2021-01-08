package com.floober.engine.renderEngine.fonts.fontMeshCreator;

import com.floober.engine.util.data.Pair;

import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {

	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;
	protected final static int NEWLINE_ASCII = 10;

	private final MetaFile metaData;

	protected TextMeshCreator(String metaFile) {
		metaData = new MetaFile(metaFile);
	}

	protected TextMeshData createTextMesh(GUIText text) {
		List<Line> lines = createStructure(text);
		TextMeshData textMeshData = createQuadVertices(text, lines);
		text.setTextMeshData(textMeshData);
		return textMeshData;
	}

	private List<Line> createStructure(GUIText text) {
		char[] chars = text.getTextString().toCharArray();
		List<Line> lines = new ArrayList<>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			if (c == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
			}
			else if (c == NEWLINE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				lines.add(currentLine);
				currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
				if (!added) currentLine.attemptToAddWord(currentWord);
				currentWord = new Word(text.getFontSize());
			}
			else {
				Character character = metaData.getCharacter(c);
				currentWord.addCharacter(character);
			}
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if (!added) {
			if (currentLine.getLineLength() > 0) lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
			added = currentLine.attemptToAddWord(currentWord);
			if (!added) { // if the word is too long, break it up!
				// get the line size for convenience
				float lineSize = text.getMaxLineSize();
				// get the largest subword we can fit on one line, and add it
				Pair<Word, Word> subwords = currentWord.getMaxLengthSubword(lineSize);
				currentLine.attemptToAddWord(subwords.data1());
				lines.add(currentLine);
				// then, for the remainder of this word, repeat the process until it all fits
				while (subwords.data2().getWordWidth() > 0) {
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
					subwords = subwords.data2().getMaxLengthSubword(lineSize);
					currentLine.attemptToAddWord(subwords.data1());
					lines.add(currentLine);
				}
			}
		}
		lines.add(currentLine);
	}

	private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double cursorX = 0f;
		double cursorY = 0f;
		List<Float> vertices = new ArrayList<>();
		List<Float> textureCoords = new ArrayList<>();
		for (Line line : lines) {
			if (text.isCentered()) {
				cursorX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(cursorX, cursorY, text.getPosition().z, letter, text.getFontSize(), vertices);
					addTexCoords(textureCoords, letter.xTextureCoord(), letter.yTextureCoord(),
							letter.xMaxTextureCoord(), letter.yMaxTextureCoord());
					cursorX += letter.xAdvance() * text.getFontSize();
				}
				cursorX += metaData.getSpaceWidth() * text.getFontSize();
			}
			cursorX = 0;
			cursorY += LINE_HEIGHT * text.getFontSize();
		}
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords), (float) cursorY);
	}

	private void addVerticesForCharacter(double cursorX, double cursorY, float z, Character character, double fontSize,
										 List<Float> vertices) {
		double x = cursorX + (character.xOffset() * fontSize);
		double y = cursorY + (character.yOffset() * fontSize);
		double maxX = x + (character.sizeX() * fontSize);
		double maxY = y + (character.sizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, z, properMaxX, properMaxY);
	}

	private static void addVertices(List<Float> vertices, double x, double y, float z, double maxX, double maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add(z);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add(z);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add(z);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add(z);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add(z);
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add(z);
	}

	private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}
	
	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}

}