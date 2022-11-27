package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.OutlineElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.RectElement;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.util.AnchorPoint;
import com.gnix.jflatgl.core.util.color.Colors;
import com.gnix.jflatgl.core.util.data.Queue;
import com.gnix.jflatgl.core.input.KeyInput;
import com.gnix.jflatgl.core.util.time.Timer;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.GUIAction;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TextInputComponent extends GUIComponent {

	// visual elements
	private final GUIText text;
	private final OutlineElement borderElement;
	private final RectElement baseElement;
	private final RectElement cursorElement;

	private final Vector4f textBorderColor = new Vector4f();
	private final Vector4f cursorColor = new Vector4f();

	private final Timer cursorTimer;
	private boolean showCursor = true;

	// font size
	private float defaultTextSize = 1;

	// text padding
	private float textPaddingPixels;
	private float textScreenSpace;

	// border size
	private float borderWidth;

	// store the text locally
	private final List<Character> inputText;
	private int charLimit = 1000;

	// control cursor position
	private int cursorIndex;

	// reacting to inputs
	private GUIAction onTextChange = () -> {};
	private boolean textChanged;

	// overflow mode
	public enum OverflowMode {
		WRAP, SCROLL
	}
	private OverflowMode overflowMode;
	private float textCenterOffset = 0;

	public TextInputComponent(String componentID, GUI parent) {
		super(componentID, parent);
		inputText = new ArrayList<>();
		text = new GUIText("", 1, Game.getFont("default"), new Vector3f(), 1);
		text.setUseStencil(true);
		text.setAnchorPoint(AnchorPoint.CENTER);
		borderElement = new OutlineElement(Colors.INVISIBLE, 0, 0, 0, 0, 0, 0, true);
		baseElement = new RectElement(Colors.INVISIBLE, 0, 0, 0, 0, 0, true);
		cursorElement = new RectElement(Colors.INVISIBLE, 0, 0, 0, 0, 0, true);
		overflowMode = OverflowMode.WRAP;
		cursorTimer = new Timer(0.5f);
		cursorTimer.start();
		// default left click behavior is to focus this component
		onLeftClick(() -> parent.focusComponent(this));
		// default focus action is to show the cursor and restart the cursor timer
		onFocus(() -> {
			showCursor = true;
			cursorTimer.restart();
		});
	}

	/**
	 * Assign a GUIAction to be executed when the user changes the text
	 * in this input field.
	 * @param action the action to take when the text is changed
	 * @return this
	 */
	public TextInputComponent onTextChange(GUIAction action) {
		onTextChange = action;
		return this;
	}

	// REACTING TO INPUT
	@Override
	public void checkInput() {
		super.checkInput();
		if (textChanged) {
			onTextChange.onTrigger();
			textChanged = false;
		}
	}

	// ******************************** BUILDING ********************************
	public TextInputComponent cursorColor(Vector4f color) {
		setCursorColor(color);
		return this;
	}

	/**
	 * Set the primary color of this TextInputComponent. The primary color is used as
	 * the color for the input text.
	 * @param primaryColor the color of the text
	 */
	@Override
	public void setPrimaryColor(Vector4f primaryColor) {
		super.setPrimaryColor(primaryColor);
	}

	/**
	 * Set the secondary color of this TextInputComponent. The secondary color is used as
	 * the color of the border/outline.
	 * @param secondaryColor the border color
	 */
	@Override
	public void setSecondaryColor(Vector4f secondaryColor) {
		super.setSecondaryColor(secondaryColor);
	}

	/**
	 * Set the tertiary color of this TextInputComponent. The tertiary color is used as
	 * the color of the background.
	 * @param tertiaryColor the background color
	 */
	@Override
	public void setTertiaryColor(Vector4f tertiaryColor) {
		super.setTertiaryColor(tertiaryColor);
	}

	// ******************************** GETTERS ********************************
	public Vector4f getCursorColor() {
		return new Vector4f(cursorColor);
	}

	public int getCharLimit() {
		return charLimit;
	}

	public OverflowMode getOverflowMode() {
		return overflowMode;
	}

	// ******************************** SETTERS ********************************
	public void setFont(FontType font) { text.setFont(font); }

	public void setFontSize(float fontSize) {
		defaultTextSize = fontSize;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	public void setTextAlignment(GUIText.Alignment alignment) {
		text.setTextAlignment(alignment);
	}

	public void setTextAnchorPoint(AnchorPoint anchorPoint) {
		text.setAnchorPoint(anchorPoint);
	}

	public void setTextPaddingPixels(float textPaddingPixels) {
		this.textPaddingPixels = textPaddingPixels;
		if (overflowMode == OverflowMode.WRAP) {
			// figure out what the horizontal space available for text is in pixels
			float textPixelSpace = getScaledWidth() - textPaddingPixels * 2;
			// convert pixels to screen space
			float textScreenSpace = DisplayManager.convertToScreenSize(textPixelSpace, false);
			// set the line max size
			text.setLineMaxSize(textScreenSpace);
		}
	}

	public void setOverflowMode(OverflowMode overflowMode) {
		this.overflowMode = overflowMode;
		if (this.overflowMode == OverflowMode.WRAP) {
			// no offset
			textCenterOffset = 0;
			// figure out what the horizontal space available for text is in pixels
			float textPixelSpace = getScaledWidth() - textPaddingPixels * 2;
			// convert pixels to screen space
			float textScreenSpace = DisplayManager.convertToScreenSize(textPixelSpace, false);
			// set the line max size
			text.setLineMaxSize(textScreenSpace);
		}
	}

	/**
	 * Set the maximum length of the String this TextInputComponent
	 * will accept. The user will not be able to enter more characters
	 * once the String held in the input field reaches this size.
	 * @param charLimit the maximum String length
	 */
	public void setCharLimit(int charLimit) {
		this.charLimit = charLimit;
	}

	public void setTextWidth(float width) {
		text.setWidth(width);
	}

	public void setTextEdge(float edge) {
		text.setEdge(edge);
	}

	public void setTextBorderColor(Vector4f color) { textBorderColor.set(color); }

	public void setTextBorderWidth(float borderWidth) {
		text.setBorderWidth(borderWidth);
	}

	public void setTextBorderEdge(float borderEdge) {
		text.setBorderEdge(borderEdge);
	}

	public void setCursorColor(Vector4f color) {
		cursorColor.set(color);
	}

	public void setCursorBlinkTime(float time) {
		cursorTimer.restart(time);
	}

	/**
	 * Get the String currently present within this input field.
	 * @return the input as a String
	 */
	public String getInputString() {
		StringBuilder temp = new StringBuilder();
		for (Character ch : inputText) temp.append(ch);
		return temp.toString();
	}

	public void setInputString(String inputString) {
		int prevSize = inputText.size();
		inputText.clear();
		for (char c : inputString.toCharArray()) {
			inputText.add(c);
		}
		cursorIndex = inputString.length();

		// update the text
		text.replaceText(getInputString());
		text.update();

		// handle the resize
		handleTextResize(prevSize);
	}

	// UTILITY
	public void clear() {
		inputText.clear();
		text.replaceText("");
		cursorIndex = 0;
	}

	// ******************************** OVERRIDES ********************************
	@Override
	public void handleKeyInput() {

		int prevSize = inputText.size();
		int prevCursor = cursorIndex;

		// handle cursor moves (arrow keys, ctrl, home/end, pg up/down)
		if (KeyInput.isPressed(KeyInput.LEFT)) {

			// if CTRL is held, jump back a word
			if (KeyInput.isCtrl()) {

				String textStr = getInputString();

				// find the most recent space before the cursor
				String preceding = textStr.substring(0, cursorIndex);
				int previousSpace = preceding.lastIndexOf(' ');

				// if there is a space before, jump there
				if (previousSpace != -1) cursorIndex = previousSpace;
				// otherwise jump to the start
				else cursorIndex = 0;

			}
			else {
				// move back 1
				cursorIndex--;
				// but not past 0
				if (cursorIndex < 0) cursorIndex = 0;
//				else Logger.log("Cursor left 1");
			}

		}
		if (KeyInput.isPressed(KeyInput.RIGHT)) {

			String textStr = getInputString();

			// if CTRL is held, jump forward a word
			if (KeyInput.isCtrl()) {

				// find the next space after the cursor
				String following = textStr.substring(cursorIndex);
				int nextSpace = following.indexOf(' ');

				// if there is a space after, jump to it (+1 to move after it)
				if (nextSpace != -1) cursorIndex = nextSpace + 1;
				// otherwise jump to the end
				else cursorIndex = textStr.length();

			}
			else {
				// move forward 1
				cursorIndex++;
				// but not past the end
				if (cursorIndex > textStr.length()) cursorIndex = textStr.length();
//				else Logger.log("Cursor right 1");
			}

		}

		// HOME and END keys
		if (KeyInput.isPressed(KeyInput.HOME)) {
			cursorIndex = 0;
		}
		if (KeyInput.isPressed(KeyInput.END)) {
			cursorIndex = inputText.size();
		}

		// check for new text from the KeyInput utility
		Queue<Character> newText = KeyInput.getCharacterInputQueue();

		if (!newText.isEmpty()) {

			// ********** perform validation checks! **********

			// build a String out of every character that will fit
			while (newText.hasNext()) {

				char ch = newText.poll();
				if (ch == '\b') { // backspace

					// delete the first character left of the cursor if one exists
					if (cursorIndex > 0) {
						inputText.remove(cursorIndex - 1);
						cursorIndex--;
					}

				}
				else if (ch == '\u007F') { // delete

					// delete the first character right of the cursor if one exists
					if (cursorIndex < inputText.size()) {
						inputText.remove(cursorIndex);
					}

				}
				else if (inputText.size() < charLimit) {
					inputText.add(cursorIndex, ch);
					cursorIndex++;
				}

				textChanged = true;

			}

		}

		// update the text
		text.replaceText(getInputString());
		text.update();

		if (cursorIndex != prevCursor || inputText.size() != prevSize) {
			// hold cursor visible while editing text or moving the cursor
			showCursor = true;
			cursorTimer.restart();
		}


		if (inputText.size() != prevSize) {

			// HANDLE RESIZE
			handleTextResize(prevSize);

		}

	}

	private void handleTextResize(int prevSize) {
		// HANDLE RESIZE
		if (overflowMode == OverflowMode.SCROLL) {

			if (inputText.size() > prevSize) { // TEXT GREW

				// if the text spilled over onto the next line, right-justify
				if (text.getTextMeshData().textLines().size() > 1) {

					text.setTextAlignment(GUIText.Alignment.RIGHT);
					text.setLineMaxSize(100);

					textCenterOffset = -text.getLineWidth() / 2f;

//						Logger.log("Overflow! Offsetting text.");

				}

			}
			else { // TEXT SHRANK

				// if the text now fits in a single line, left-justify
				if (text.getTextMeshData().textLines().get(0).getLineLength() < textScreenSpace) {

					text.setTextAlignment(GUIText.Alignment.LEFT);
					text.setLineMaxSize(textScreenSpace);
					textCenterOffset = 0;

//						Logger.log("Fits! Restricting line width");

				}

			}

		}
	}

	@Override
	public void update() {
		if (isFocused()) {
			if (cursorTimer.finished()) {
				showCursor = !showCursor;
				cursorTimer.restart();
			}
		}
		else {
			showCursor = true;
		}
	}

	@Override
	public void doTransform() {
		// ********** position the elements **********
		// figure out what the horizontal space available for text is in pixels
		float textPixelSpace = getScaledWidth() - textPaddingPixels * 2;
		// convert pixels to screen space
		textScreenSpace = DisplayManager.convertToScreenSize(textPixelSpace, false);

		// text
		float textPosX = getX();
		float textPosY = getY();

		if (text.getTextAlignment() == GUIText.Alignment.RIGHT) {
			textPosX = getRight() - borderWidth * getScale() - textPaddingPixels * getScale();
		}
		else {
			if (overflowMode == OverflowMode.WRAP)
				textPosY = getTop() + (borderWidth + textPaddingPixels) * getScale();
		}

//		if (!text.isCentered()) {
//			textPosX = getLeft() + borderWidth * getScale() + textPaddingPixels * getScale();
//		}

		Vector2f textPos = DisplayManager.convertToTextScreenPos(new Vector2f(textPosX, textPosY));
		textPos.setComponent(0, textPos.x + textCenterOffset);
//		Logger.log("textPos = " + textPos.x);

		text.setPosition(new Vector3f(textPos, getLayer() + 1));
//		text.center();

		// border
		borderElement.setPosition(getPosition());
		borderElement.setLayer(getLayer() + 1);
		// base
		baseElement.setPosition(getPosition());
		// cursor
		Vector2f cursorPos = text.getCursorPosition(cursorIndex);
//		cursorPos.x -= text.getLineWidth() / 2f;
		cursorPos.mul(2);
		cursorPos.sub(1, 1);

		Vector3f cursorPixelPos = DisplayManager.convertToPixelPosition(cursorPos, getLayer() + 2);
		cursorPixelPos.setComponent(1, getY());
		cursorElement.setPosition(cursorPixelPos);

		// ********** resize the elements **********
		text.setFontSize(defaultTextSize * getScale());
		borderElement.setSize(getScaledSize());
		borderElement.setLineWidth(borderWidth * getScale());
		baseElement.setSize(getScaledSize());

		cursorElement.setSize(5 * getScale(), 40 * getScale());

		// ********** color the elements **********
		text.setColor(getPrimaryColor().mul(getOpacity()));
		text.setOutlineColor(textBorderColor.mul(getOpacity()));

		borderElement.setColor(getSecondaryColor().mul(getOpacity()));
		baseElement.setColor(getTertiaryColor().mul(getOpacity()));
		cursorElement.setColor(getCursorColor().mul(getOpacity()));

		// ********** transform the elements **********
		borderElement.transform();
		baseElement.transform();
		cursorElement.transform();

		// set the stencil region for the text
		Vector4f stencilRegion = new Vector4f(getX(), getY(), getScaledWidth(), getScaledHeight());
		text.setStencilRegion(stencilRegion);

	}

	@Override
	public void render() {
		if (text.isHidden()) text.show();
		borderElement.render();
		baseElement.render();
		if (showCursor && isFocused()) cursorElement.render();
	}

	@Override
	public void remove() {
		text.remove();
	}

	@Override
	public void restore() {
		text.show();
	}
}
