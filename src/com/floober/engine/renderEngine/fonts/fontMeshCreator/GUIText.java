package com.floober.engine.renderEngine.fonts.fontMeshCreator;

import com.floober.engine.game.Game;
import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.models.ModelLoader;
import com.floober.engine.util.Logger;
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

	private String textString;
	private float fontSize;
	
	private int firstCharVisible;
	private int lastCharVisible;

	private TextMeshData textMeshData;

	private int textMeshVao;
	private final List<Integer> textMeshVbos = new ArrayList<>();
	private int vertexCount;
	private final Vector4f color = new Vector4f(0f, 0f, 0f, 1f);

	private final Vector3f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private final boolean centerText;

	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;

	private final Vector2f shadowOffset;
	private final Vector4f outlineColor;

	private boolean hidden;
	private boolean processed;
	private boolean needsReload;

	public GUIText() {
		this.textString = "";
		this.fontSize = 1;
		this.font = Game.getFont("default");
		this.position = new Vector3f();
		this.lineMaxSize = 1;
		this.centerText = false;
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
		this.centerText = false;
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
	 * @param centered Whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector3f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
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

	// GETTERS
	public float getWidth() {
		return width;
	}
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

	// SETTERS

	// Category 1: Free Setters (no need to update vertex data)
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}
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

	public void revealNextChar() {
		lastCharVisible++;
	}

	// Category 2: Damaging Setters (vertex data must be updated)
	public void replaceText(String newText) {
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
		reload(oldFont); // try this immediately
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
		this.lineMaxSize = lineMaxSize;
		needsReload = true;
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the color of the text.
	 * 
	 * @param r Red value, between 0 and 1.
	 * @param g Green value, between 0 and 1.
	 * @param b Blue value, between 0 and 1.
	 */
	public void setColor(float r, float g, float b, float a) {
		color.set(r, g, b, a);
	}

	public void setColor(Vector4f color) {
		setColor(color.x(), color.y(), color.z(), color.w());
	}

	/**
	 * @return the colour of the text.
	 */
	public Vector4f getColor() {
		return color;
	}

	/**
	 * @return The number of lines of text. This is determined when the text is
	 *         loaded, based on the length of the text and the max line length
	 *         that is set.
	 */
	public int getNumberOfLines() {
		return numberOfLines;
	}

	/**
	 * @return The position of the top-left corner of the text in screen-space.
	 *         (0, 0) is the top left corner of the screen, (1, 1) is the bottom
	 *         right.
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return the ID of the text's VAO, which contains all the vertex data for
	 *         the quads on which the text will be rendered.
	 */
	public int getMesh() {
		return textMeshVao;
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
	 * @return The total number of vertices of all the text's quads.
	 */
	public int getVertexCount() {
		return this.vertexCount;
	}

	/**
	 * @return the font size of the text (a font size of 1 is normal).
	 */
	protected float getFontSize() {
		return fontSize;
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

	/**
	 * @return {@code true} if the text should be centered.
	 */
	protected boolean isCentered() {
		return centerText;
	}

	/**
	 * @return The maximum length of a line of this text.
	 */
	protected float getMaxLineSize() {
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

	// ACTIONS
	/**
	 * Show the text on the screen. If it is already shown, it will be removed first.
	 */
	public void show() {
		if (!hidden) remove();
		TextMaster.loadText(this);
		hidden = false;
	}

	/**
	 * Remove the text from the screen.
	 */
	public void hide() {
		TextMaster.removeText(this);
		hidden = true;
	}

	/**
	 * Remove the text from the screen and delete it.
	 */
	public void remove() {
		TextMaster.removeText(this);
		delete();
	}

	private void remove(FontType oldFont) {
		TextMaster.removeText(this, oldFont);
		delete();
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

	/**
	 * This should be called each frame, after any modifications are made.
	 * If this GUIText has been modified in a way that requires rebuilding
	 * its mesh data, this method will do so, then reset the flag.
	 *
	 * @return {@code true} if this text was reloaded.
	 */
	public boolean update() {
		if (needsReload) {
			reload();
			needsReload = false;
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
	 * Center the text horizontally on its current position.
	 * Should only be called once per position change.
	 */
	public void center() {
		if (centerText) {
			// x is easy
			position.x -= lineMaxSize / 2;
			// y is also easy, now
			try {
				float textHeight = textMeshData.textHeight();
				position.y -= textHeight / 2;
				if (textString.startsWith("Floober101")) Logger.log("text y = " + position.y);
			} catch (Exception e) {
				// guess we're not ready for that yet
				Logger.logWarning("Maybe don't center this text yet, if you can help it");
			}
			// mark for reloading
			needsReload = true;
		}
	}

	/**
	 * Add a vbo to the vbo list. I don't remember why I needed to add this, but apparently I did.
	 * @param vbo The ID of the vbo to add.
	 */
	public void addVbo(int vbo) {
		textMeshVbos.add(vbo);
	}

}