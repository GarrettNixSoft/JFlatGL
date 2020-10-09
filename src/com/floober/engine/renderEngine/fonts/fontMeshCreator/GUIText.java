package com.floober.engine.renderEngine.fonts.fontMeshCreator;

import com.floober.engine.renderEngine.fonts.fontRendering.TextMaster;
import com.floober.engine.renderEngine.models.ModelLoader;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

/**
 * Represents a piece of text in the game.
 * 
 * @author Karl
 *
 */
public class GUIText {

	private String textString;
	private final float fontSize;

	private int textMeshVao;
	private final List<Integer> textMeshVbos = new ArrayList<>();
	private int vertexCount;
	private final Vector4f color = new Vector4f(0f, 0f, 0f, 1f);

	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;

	private FontType font;

	private boolean centerText = false;

	private float width;
	private float edge;
	private float borderWidth;
	private float borderEdge;

	private final Vector2f shadowOffset;
	private final Vector4f outlineColor;

	private boolean needsReload;

	/**
	 * Creates a new text, loads the text's quads into a VAO, and adds the text
	 * to the screen.
	 * 
	 * @param text
	 *            - the text.
	 * @param fontSize
	 *            - the font size of the text, where a font size of 1 is the
	 *            default size.
	 * @param font
	 *            - the font that this text should use.
	 * @param position
	 *            - the position on the screen where the top left corner of the
	 *            text should be rendered. The top left corner of the screen is
	 *            (0, 0) and the bottom right is (1, 1).
	 * @param maxLineLength
	 *            - basically the width of the virtual page in terms of screen
	 *            width (1 is full screen width, 0.5 is half the width of the
	 *            screen, etc.) Text cannot go off the edge of the page, so if
	 *            the text is longer than this length it will go onto the next
	 *            line. When text is centered it is centered into the middle of
	 *            the line, based on this line length value.
	 * @param centered
	 *            - whether the text should be centered or not.
	 */
	public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
		this.textString = text;
		this.fontSize = fontSize;
		this.font = font;
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
		// default values
		this.width = 0.5f;
		this.edge = 0.01f;
		this.borderWidth = 0;
		this.borderEdge = 0.1f;
		this.shadowOffset = new Vector2f(0, 0);
		this.outlineColor = new Vector4f(0, 0, 0, 1);
		// load text
		TextMaster.loadText(this);
	}

	// ADDING METHODS

	/**
	 * This should be called each frame, after any modifications are made.
	 * If this GUIText has been modified in a way that requires rebuilding
	 * its mesh data, this method will do so, then reset the flag.
	 */
	public void update() {
		if (needsReload) {
			reload();
			needsReload = false;
		}
	}

	private void reload() {
		// remove old version
		remove();
		// replace with new version
		TextMaster.loadText(this);
	}

	public void addVbo(int vbo) {
		textMeshVbos.add(vbo);
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

	// SETTERS
	public void updateText(String newText) {
		// update string
		this.textString = newText;
		// mark needs reload
		needsReload = true;
	}

	public void setFont(FontType font) {
		this.font = font;
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

	public void setPosition(Vector2f position) {
		this.position = position;
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

	public void setLineMaxSize(float lineMaxSize) {
		this.lineMaxSize = lineMaxSize;
		needsReload = true;
	}

	// END_ADDING_METHODS

	/**
	 * Remove the text from the screen.
	 */
	public void remove() {
		TextMaster.removeText(this);
		ModelLoader.deleteVAO(textMeshVao);
		for (int vbo : textMeshVbos) {
			ModelLoader.deleteVBO(vbo);
		}
		textMeshVbos.clear();
	}

	/**
	 * @return The font used by this text.
	 */
	public FontType getFont() {
		return font;
	}

	/**
	 * Set the colour of the text.
	 * 
	 * @param r
	 *            - red value, between 0 and 1.
	 * @param g
	 *            - green value, between 0 and 1.
	 * @param b
	 *            - blue value, between 0 and 1.
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
	public Vector2f getPosition() {
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
	 * @param number
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

}
