package com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.fonts.fontRendering.TextMaster;
import com.gnix.jflatgl.core.renderEngine.models.ModelLoader;
import com.gnix.jflatgl.core.renderEngine.util.AnchorPoint;
import com.gnix.jflatgl.core.util.configuration.Config;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	// the actual text this GUIText represents
	private String textString;

	// the generated mesh data that the GPU needs
	private TextMeshData textMeshData;
	private int textMeshVao;
	private final List<Integer> textMeshVbos = new ArrayList<>();
	private int vertexCount;
	private final Vector4f color = new Vector4f(0f, 0f, 0f, 1f);

	// The Font to use
	private FontType font;
	private float fontSize;

	// control which portion of the text is visible (useful for scrolling text)
	private int firstCharVisible;
	private int lastCharVisible;

	// where the text is on screen
	private final Vector3f position;

	// text can be split into multiple lines
	private float lineMaxSize;
	private int numberOfLines;

	// text alignment
	public enum Alignment {
		LEFT, CENTER, RIGHT
	}
	private Alignment textAlignment;

	private AnchorPoint anchorPoint;

	// shader settings
	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;

	private final Vector2f shadowOffset;
	private final Vector4f outlineColor;

	// state flags
	private boolean hidden;
	private boolean processed;
	private boolean needsReload;
	private boolean useStencil;

	// stencil region for controlling what portion of the text is visible
	private final Vector4f stencilRegion = new Vector4f();

	// TODO fix bug where text created with this Constructor doesn't appear
	public GUIText() {
		this.textString = "";
		this.fontSize = 1;
		this.font = Game.getFont("default");
		this.position = new Vector3f();
		this.lineMaxSize = 1;
		this.textAlignment = Alignment.LEFT;
		this.anchorPoint = AnchorPoint.CENTER;
		this.firstCharVisible = 0;
		this.lastCharVisible = textString.length();
		// default values
		this.width = 0.5f;
		this.edge = 0.01f;
		this.borderWidth = 0;
		this.borderEdge = 0.1f;
		this.shadowOffset = new Vector2f(0, 0);
		this.outlineColor = new Vector4f(0, 0, 0, 1);
		this.hidden = true;
		// create mesh data
		TextMaster.processText(this);
	}

	public GUIText(String text) {
		this.textString = text;
		this.fontSize = 1;
		this.font = Game.getFont("default");
		this.position = new Vector3f();
		this.lineMaxSize = 1;
		this.textAlignment = Alignment.LEFT;
		this.anchorPoint = AnchorPoint.CENTER;
		this.firstCharVisible = 0;
		this.lastCharVisible = textString.length();
		// default values
		this.width = 0.5f;
		this.edge = 0.01f;
		this.borderWidth = 0;
		this.borderEdge = 0.1f;
		this.shadowOffset = new Vector2f(0, 0);
		this.outlineColor = new Vector4f(0, 0, 0, 1);
		this.hidden = true;
		// create mesh data
		TextMaster.processText(this);
	}

	public GUIText(String text, float fontSize, FontType font, Vector3f position, float maxLineLength) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.textAlignment = Alignment.LEFT;
		this.anchorPoint = AnchorPoint.CENTER;
		this.firstCharVisible = 0;
		this.lastCharVisible = textString.length();
		// default values
		this.width = 0.5f;
		this.edge = 0.01f;
		this.borderWidth = 0;
		this.borderEdge = 0.1f;
		this.shadowOffset = new Vector2f(0, 0);
		this.outlineColor = new Vector4f(0, 0, 0, 1);
		this.hidden = true;
		// create mesh data
		TextMaster.processText(this);
	}

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 *
	 * @param text The text.
	 * @param fontSize The font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font The font that this text should use.
	 * @param position The position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength Basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param textAlignment The text justify setting: LEFT, CENTER, or RIGHT
	 */
	public GUIText(String text, float fontSize, FontType font, Vector3f position, float maxLineLength, Alignment textAlignment) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.textAlignment = textAlignment;
		this.anchorPoint = AnchorPoint.CENTER;
		this.firstCharVisible = 0;
		this.lastCharVisible = textString.length();
		// default values
		this.width = 0.5f;
		this.edge = 0.01f;
		this.borderWidth = 0;
		this.borderEdge = 0.1f;
		this.shadowOffset = new Vector2f(0, 0);
		this.outlineColor = new Vector4f(0, 0, 0, 1);
		this.hidden = true;
		// create mesh data
		TextMaster.processText(this);
	}

	/**
	 * Create a new GUIText with new text, copying all other settings
	 * from another GUIText object.
	 * @param other the GUIText to copy settings from
	 * @param newTextString the new text to display
	 */
	public GUIText(GUIText other, String newTextString) {
		this.textString = newTextString;
		this.fontSize = other.fontSize;
		this.font = other.font;
		this.position = other.position;
		this.lineMaxSize = other.lineMaxSize;
		this.textAlignment = other.textAlignment;
		this.firstCharVisible = 0;
		this.lastCharVisible = newTextString.length();
		// text display values
		this.width = other.width;
		this.edge = other.edge;
		this.borderWidth = other.borderWidth;
		this.borderEdge = other.borderEdge;
		this.shadowOffset = other.shadowOffset;
		this.outlineColor = other.outlineColor;
		this.hidden = other.hidden;
		// create mesh data
		TextMaster.processText(this);
	}

	// GETTERS

	public TextMeshData getTextMeshData() {
		return textMeshData;
	}

	public int getLayer() {
		return (int) this.position.z();
	}
	public float getLineWidth() {
		return lineMaxSize;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() { return textMeshData.textHeight(); }
	public float getEdge() {
		return edge;
	}
	public float getBorderWidth() {
		return borderWidth;
	}
	public float getBorderEdge() {
		return borderEdge;
	}
	public Vector4f getOutlineColor() {
		return outlineColor;
	}
	public Vector2f getShadowOffset() {
		return shadowOffset;
	}
	public int getFirstCharVisible() {
		return firstCharVisible;
	}
	public int getLastCharVisible() {
		return lastCharVisible;
	}
	public int getNumVisibleChars() {
		return lastCharVisible - firstCharVisible;
	}
	public boolean isProcessed() { return processed; }
	public Vector4f getStencilRegion() { return stencilRegion; }
	public Vector2f getStencilPos() {
		return new Vector2f(stencilRegion.x, stencilRegion.y);
	}
	public Vector2f getStencilSize() {
		return new Vector2f(stencilRegion.z, stencilRegion.w);
	}

	public float getPixelWidth() {
		return lineMaxSize * Config.INTERNAL_WIDTH;
	}

	public float getPixelHeight() {
		return textMeshData.textHeight() * Config.INTERNAL_HEIGHT;
	}

	public boolean nextCharNotWhitespace(int charIndex) {
		return (charIndex < getTextString().length() && getTextString().charAt(charIndex) != ' ') || charIndex == getTextString().length();
	}

	public Alignment getTextAlignment() {
		return textAlignment;
	}

	public AnchorPoint getAnchorPoint() {
		return anchorPoint;
	}

	public boolean isCenteredVertical() {
		return anchorPoint == AnchorPoint.CENTER_LEFT || anchorPoint == AnchorPoint.CENTER || anchorPoint == AnchorPoint.CENTER_RIGHT;
	}

	public int getVertexCount() {
		return this.vertexCount;
	}
	public float getFontSize() {
		return fontSize;
	}
	public FontType getFont() {
		return font;
	}
	public Vector4f getColor() {
		return color;
	}
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector3f getPosition() {
		return new Vector3f(position);
	}

	public Vector2f getPosition2() {
		return new Vector2f(position.x, position.y);
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
	}

	/**
	 * @return {@code true} if the text should be centered.
	 */
	public boolean isCentered() {
		return textAlignment == Alignment.CENTER;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	public float getLineMaxSize() {
		return lineMaxSize;
	}

	/**
	 * @return The string of text.
	 */
	public String getTextString() {
		return textString;
	}

	/**
	 * @return Whether this text is currently hidden.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @return Whether this text currently uses the stencil test.
	 */
	public boolean isUseStencil() { return useStencil; }

	public Vector2f getCursorPosition(int cursorIndex) {

		double x = 0;
		double y = 0;

		double spaceWidth = font.getMetaData().getSpaceWidth() * fontSize;
		double lineHeight = TextMeshCreator.LINE_HEIGHT * fontSize;

		List<Line> lines = textMeshData.textLines();
		Line currentLine = lines.get(0);

		if (currentLine.isEmpty()) {
			return getPosition2();
		}

		List<Word> words = currentLine.getWords();
		Word currentWord = words.get(0);

		if (currentWord.isEmpty()) {
			return getPosition2();
		}

		List<Character> characters = currentWord.getCharacters();
		Character currentCharacter;

		// adjust starting position
		if (!isCenteredVertical()) {
			double totalHeight = lines.size() * lineHeight;
			y -= totalHeight / 2;
		}
		if (textAlignment == Alignment.RIGHT) {
			x = lineMaxSize - currentLine.getLineLength();
		}

		// parse characters
		int lineIndex = 0;
		int wordIndex = 0;
		int charIndex = 0;
		int index = 0;

		// count the index until the target is reached
		while (index < cursorIndex) {

			// if this index is beyond the size of the current word, move on to the next one
			if (charIndex >= characters.size()) {
				wordIndex++;
				charIndex = 0;

				if (wordIndex < words.size()) { // remain on the same line

					// skip every space between the current word and the next
					currentWord = words.get(wordIndex);
					index += currentWord.getSpacesBefore();
					x += currentWord.getSpacesBefore() * spaceWidth;

//					Logger.log("Spaces before: " + currentWord.getSpacesBefore());

					if (currentWord.isEmpty()) { // there was a trailing space, we've reached the end
						return new Vector2f((float) x, (float) y).add(getPosition2());
					}
					else {
						characters = currentWord.getCharacters();
						currentCharacter = characters.get(charIndex);
					}



				}
				else { // move to the next line
					lineIndex++;

					if (lineIndex < lines.size()) { // still in bounds

						// wrap word and character index
						wordIndex = 0;

						// wrap x back around
						if (textAlignment == Alignment.RIGHT)
							x = lineMaxSize - currentLine.getLineLength();
						else
							x = 0;
						// move y down a line
						y += lineHeight;

						// get the next line
						currentLine = lines.get(lineIndex);

						if (currentLine.isEmpty()) {// there was a trailing space, we've reached the end
							return new Vector2f((float) x, (float) y).add(getPosition2());
						}

						// get the next word and character
						words = currentLine.getWords();
						currentWord = words.get(wordIndex);

						if (currentWord.isEmpty()) { // there was a trailing space, we've reached the end
							return new Vector2f((float) x, (float) y).add(getPosition2());
						}
						else {
							characters = currentWord.getCharacters();
							currentCharacter = characters.get(charIndex);
						}

					}
					else { // out of bounds; return current position
						return new Vector2f((float) x, (float) y).add(getPosition2());
					}

				}

			}
			else {
				currentCharacter = characters.get(charIndex);
			}

			// add the current character's size
			x += currentCharacter.xAdvance() * fontSize;

			// advance to the next character
			index++;
			charIndex++;

		}

		// return result
		return new Vector2f((float) x, (float) y).add(getPosition2());

	}

	// SETTERS

	// Category 1: Free Setters (no need to update vertex data)
	public void setPosition(Vector3f position) {
		int oldLayer = getLayer();
		this.position.set(position);
		int newLayer = getLayer();
		if (oldLayer != newLayer) {
			TextMaster.moveLayers(this, oldLayer, newLayer);
		}
		// move to center the text properly
		align();
	}

	public void setPixelPosition(float x, float y, int layer) {
		setPosition(new Vector3f(DisplayManager.convertToTextScreenPos(new Vector2f(x, y)), layer));
	}

	public void setColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}
	public void setColor(Vector4f color) {
		setColor(color.x(), color.y(), color.z(), color.w());
	}

	public void setLayer(int layer) {this.position.setComponent(2, layer); }
	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}
	public void setBorderEdge(float borderEdge) {
		this.borderEdge = borderEdge;
	}
	public void setShadowOffset(float x, float y) {
		shadowOffset.set(x, y);
	}
	public void setOutlineColor(float r, float g, float b, float a) {
		outlineColor.set(r, g, b, a);
	}
	public void setOutlineColor(Vector4f color) {
		setOutlineColor(color.x(), color.y(), color.z(), color.w());
	}
	public void setTextMeshData(TextMeshData textMeshData) {
		this.textMeshData = textMeshData;
	}
	public void setFirstCharVisible(int firstCharVisible) {
		this.firstCharVisible = firstCharVisible;
	}
	public void setLastCharVisible(int lastCharVisible) {
		this.lastCharVisible = lastCharVisible;
	}
	public void setProcessed(boolean processed) { this.processed = processed; }
	public void setUseStencil(boolean useStencil) {this.useStencil = useStencil; }

	public void setStencilRegion(Vector4f stencilRegion) {
		this.stencilRegion.set(stencilRegion);
	}

	public void setAlpha(float alpha) { color.setComponent(3, alpha); }

	public void revealNextChar() {
		lastCharVisible++;
	}

	/**
	 * Move this GUIText by the given offset. The offset
	 * will be added to the current position.
	 * @param offset the offset to move by
	 */
	public void move(Vector2f offset) {
		this.position.add(offset.x(), offset.y(), 0);
	}

	// Category 2: Damaging Setters (vertex data must be updated)
	public void replaceText(String newText) {
		// do nothing if the text is identical
		if (newText.equals(textString)) return;
		// update string
		this.textString = newText;
		this.lastCharVisible = textString.length();
		// mark needs reload
		processed = false;
		needsReload = true;
	}

	public void setFont(FontType font) {
		FontType oldFont = this.font;
		this.font = font;
		needsReload = true;
//		reload(oldFont); // try this immediately
	}

	public void setFontSize(float size) {
		this.fontSize = size;
		needsReload = true;
	}

	public void setWidth(float width) {
		this.width = width;
		if (this.width < 0) this.width = 0;
	}

	public void setEdge(float edge) {
		this.edge = edge;
		if (this.edge < 0.001) this.edge = 0.001f;
	}

	public void setLineMaxSize(float lineMaxSize) {
		// validate // TODO improve this based on font size and current font (minimum should be the size of the largest character in the font at the current font size)
		if (lineMaxSize == 0) return;
		// reload if the new line size setting is different
		if (this.lineMaxSize != lineMaxSize) needsReload = true;
		this.lineMaxSize = lineMaxSize;
	}

	public void setTextAlignment(Alignment textAlignment) {
		// reload if the new justify setting is different
		if (this.textAlignment != textAlignment) needsReload = true;
		this.textAlignment = textAlignment;
	}

	public void setAnchorPoint(AnchorPoint anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	/**
	 * Set the VAO and vertex count for this text.
	 * 
	 * @param vao
	 *            - the VAO containing all the vertex data for the quads on
	 *            which the text will be rendered.
	 * @param verticesCount
	 *            - the total number of vertices in all of the quads.
	 */
	public void setMeshInfo(int vao, int verticesCount) {
		this.textMeshVao = vao;
		this.vertexCount = verticesCount;
	}

	/**
	 * Sets the number of lines that this text covers (method used only in
	 * loading).
	 * 
	 * @param number The number of lines.
	 */
	protected void setNumberOfLines(int number) {
		this.numberOfLines = number;
	}

	// ACTIONS
	/**
	 * Show the text on the screen. If it is already shown, it will be removed first.
	 */
	public void show() {
		if (!hidden) remove();
		TextMaster.loadText(this);
		hidden = false;
//		Logger.log("Text shown! " + textString);
//		Logger.log(getDebugString());
	}

	/**
	 * Remove the text from the screen.
	 */
	public void hide() {
		TextMaster.removeText(this);
		hidden = true;
//		Logger.log("Text hidden! " +  textString);
	}

	/**
	 * Remove the text from the screen and delete it.
	 */
	public void remove() {
		TextMaster.removeText(this);
//		Logger.log("Removed text: " + textString);
//		Logger.log(getDebugString());
		hidden = true;
	}

	private void remove(FontType oldFont) {
		TextMaster.removeText(this, oldFont);
//		Logger.log("Removed text: " + textString);
//		Logger.log(getDebugString());
	}

	/**
	 * Delete this text's vertex data. This should only be called
	 * if this text object is guaranteed not to be rendered again.
	 * (i.e. if the TextMaster has been cleared and this object
	 * will soon be GC'd)
	 */
	public void delete() {
		ModelLoader.deleteVAO(textMeshVao);
		for (int vbo : textMeshVbos) {
			ModelLoader.deleteVBO(vbo);
		}
		textMeshVbos.clear();
	}

	public int getVAO() {
		return textMeshVao;
	}

	/**
	 * This should be called each frame, after any modifications are made.
	 * If this GUIText has been modified in a way that requires rebuilding
	 * its mesh data, this method will do so, then reset the flag.
	 *
	 * @return {@code true} if this text was reloaded.
	 */
	public boolean update() {

		if (needsReload) {
//			Logger.log("Reloading " + textString);
			reload();
			needsReload = false;
//			Logger.log("Reloading: " + textString);
//			Logger.log(getDebugString());
			return true;
		}
		return false;
	}

	private void reload() {
		// remove old version
		remove();
		// replace with new version
		TextMaster.loadText(this);
	}

	private void reload(FontType oldFont) {
		// remove old version
		remove(oldFont);
		// replace with new version
		TextMaster.loadText(this);
	}

	/**
	 * Align the text on its current position.
	 * Applies both the alignment and the anchor position.
	 */
	private void align() {

		// Default alignment requirement
		position.x -= lineMaxSize / 2;
		position.y -= textMeshData.textHeight() / 2;


		// Apply anchor to the horizontal
		switch (anchorPoint) {
			case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT -> {
				position.x += lineMaxSize / 2;
			}
			case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT -> {
				position.x -= lineMaxSize / 2;
			}
		}

		// apply anchor to the vertical
		switch (anchorPoint) {
			case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> {
				float textHeight = textMeshData.textHeight();
				position.y += textHeight / 2;
			}
			case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> {
				float textHeight = textMeshData.textHeight();
				position.y -= textHeight / 2;
			}
		}

//		// HORIZONTAL
//		// x is easy
//		position.x -= lineMaxSize / 2;
//		// mark for reloading
////		needsReload = true;
//
//		// VERTICAL
//		if (centeredVertical) {
//			// y is also easy, now
//			try {
//				float textHeight = textMeshData.textHeight();
//				position.y -= textHeight / 2;
//			} catch (Exception e) {
//				// guess we're not ready for that yet
//				Logger.logWarning("Maybe don't center this text yet, if you can help it");
//			}
//		}
	}

	/**
	 * Add a vbo to the vbo list. I don't remember why I needed to add this, but apparently I did.
	 * @param vbo The ID of the vbo to add.
	 */
	public void addVbo(int vbo) {
		textMeshVbos.add(vbo);
	}

	@Override
	public String toString() {
		return "[GUIText] @" + position + ", color=" + color + "; " + textString;
	}

	public String getDebugString(){
		return	"VAO: " + textMeshVao + "\n" +
				"Pos: " + position + "\n" +
				"Col: " + color + "\n" +
				"Siz: " + fontSize + "\n" +
				"Len: " + lineMaxSize + "\n" +
				"Lns: " + numberOfLines + "\n" +
				"1st: " + firstCharVisible + "\n" +
				"Lst: " + lastCharVisible + "\n" +
				"Hid: " + hidden + "\n" +
				"Prc: " + processed + "\n" +
				"Nrl: " + needsReload + "\n" +
				"Stn: " + useStencil + "\n" +
				"Reg: " + stencilRegion + "\n";
	}
}
