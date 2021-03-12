package com.floober.engine.gui.dialogue;

import com.floober.engine.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.renderEngine.textures.TextureComponent;
import org.joml.Vector4f;

/**
 * A DialogueLine stores all of the data needed to represent one line of dialogue in
 * the dialogue box: the texture to use for the character speaking, the name of the
 * character speaking, the dialogue line, the fonts to use, the color of the text, the
 * size of the text and name text, and whether to skip to the next line automatically
 * when the line is fully displayed on the screen.
 */
public class DialogueLine {

	private final TextureComponent face;
	private final String name;
	private final String line;

	private FontType nameFont;
	private FontType textFont;

	private Vector4f nameColor;
	private Vector4f textColor;
	private Vector4f backgroundColor;

	private float nameSize;
	private float textSize;

	private int charDelay;
	private boolean auto;

	public DialogueLine(TextureComponent face, String name, String line) {
		this.face = face;
		this.name = name;
		this.line = line;
	}

	// SETTERS (optional fields)
	public void setNameFont(FontType nameFont) {
		this.nameFont = nameFont;
	}
	public void setTextFont(FontType textFont) {
		this.textFont = textFont;
	}
	public void setNameColor(Vector4f nameColor) {
		this.nameColor = nameColor;
	}
	public void setTextColor(Vector4f textColor) {
		this.textColor = textColor;
	}
	public void setBackgroundColor(Vector4f backgroundColor) { this.backgroundColor = backgroundColor; }
	public void setNameSize(float nameSize) {
		this.nameSize = nameSize;
	}
	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}
	public void setCharDelay(int charDelay) {
		this.charDelay = charDelay;
	}
	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	// GETTERS
	public TextureComponent getFace() {
		return face;
	}
	public String getName() {
		return name;
	}
	public String getLine() {
		return line;
	}
	public FontType getNameFont() {
		return nameFont;
	}
	public FontType getTextFont() {
		return textFont;
	}
	public Vector4f getNameColor() {
		return nameColor;
	}
	public Vector4f getTextColor() {
		return textColor;
	}
	public Vector4f getBackgroundColor() { return backgroundColor; }
	public float getNameSize() {
		return nameSize;
	}
	public float getTextSize() {
		return textSize;
	}
	public int getCharDelay() {
		return charDelay;
	}
	public boolean isAuto() {
		return auto;
	}

}