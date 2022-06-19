package com.floober.engine.gui.dialogue;

import com.floober.engine.core.renderEngine.display.DisplayManager;
import com.floober.engine.core.renderEngine.display.Window;
import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.Render;
import com.floober.engine.core.renderEngine.elements.TextureElement;
import com.floober.engine.core.renderEngine.elements.geometry.RectElement;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.floober.engine.core.renderEngine.util.Layers;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.input.KeyInput;
import com.floober.engine.core.util.input.MouseInput;
import com.floober.engine.core.util.time.TimeScale;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Dialogue {

	// settings
	private final DialogueConfiguration configuration;

	// control display
	private DialogueLine currentLine;
	private final DialogueLine[] lines;
	private int currentLineIndex;

	// display text
	private GUIText dialogueText;
	private GUIText nameText;
	private int currentChar;
	private int lineLength;

	// state flags
	private boolean open, opening;
	private boolean closing, advancing, waiting;
	private boolean inMenu;

	// transition times
	private final int openTime = 300;
	private final int closeTime = 300; // TODO determine if I even need this
	private int charDelay = Settings.getSettingInt("dialogue_char_delay");

	// timing
	private long timer;
	private int duration;

	// fonts
	private static final float TEXT_FONT_SIZE_DEFAULT = 1.15f;
	private static final float NAME_FONT_SIZE_DEFAULT = 1.2f;

	private FontType nameFont;
	private FontType textFont;
	private float nameSize;
	private float textSize;

	// background color
	private final Vector4f backgroundColor;
	private final Vector4f nameColor;
	private final Vector4f textColor;

	// coordinates and size
	private int x, y, z;
	private int width, height;

	// positioning
	private int faceX, faceY;
	private int faceWidth, faceHeight;

	// elements
	private RectElement boxElement;
	private TextureElement faceElement;

	// arrow indicating more dialogue
	private Arrow arrow;

	// TODO add an event sub-queue to Dialogue; allows events to occur in response to dialogue lines, and they can overlap

	// create Dialogue box
	public Dialogue(DialogueLine[] lines, DialogueConfiguration configuration) {
		this.lines = lines;
		this.configuration = configuration;
		// default fonts and colors
		nameFont = Game.getFont("dialogue_name");
		textFont = Game.getFont("dialogue_text");
		backgroundColor = new Vector4f(0.05f, 0.05f, 0.05f, 0.5f);
		nameColor = new Vector4f(Colors.WHITE);
		textColor = new Vector4f(Colors.WHITE);
		// set up for first line
		currentLineIndex = -1;
		// create elements
		setConfiguration();
		setLocationAndSize();
		initElements();
	}

	private void setConfiguration() {
		textFont = configuration.defaultTextFont();
		nameFont = configuration.defaultNameFont();
		textColor.set(configuration.defaultTextColor());
		nameColor.set(configuration.defaultNameColor());
		textSize = configuration.defaultTextSize();
		nameSize = configuration.defaultNameSize();
	}

	private void initElements() {
		setLocationAndSize();
		boxElement = new RectElement(backgroundColor, x, y, z + 200, width, height, true);
		faceElement = new TextureElement(lines[0].getFace(), faceX, faceY, z - 50, faceWidth, faceHeight, false);
		faceElement.setHasTransparency(true);
		dialogueText = new GUIText("", TEXT_FONT_SIZE_DEFAULT * textSize, textFont, new Vector3f(0.36f, 0.73f, 0f), 0.35f, false);
		dialogueText.setWidth(0.5f);
		dialogueText.setEdge(0.2f);
		dialogueText.setColor(textColor);
		nameText = new GUIText("", NAME_FONT_SIZE_DEFAULT * nameSize, nameFont, new Vector3f(0.309f, 0.862f, 0f), 0.1f, true);
		nameText.center();
		nameText.setWidth(0.5f);
		nameText.setEdge(0.2f);
		nameText.setColor(nameColor);
	}

	private void setLocationAndSize() {
		Window gameWindow = DisplayManager.getPrimaryGameWindow();
		// box location and size
		width = (int) Math.min(gameWindow.getWidth() * 0.6, 900); // 60% of window width, up to 900px
		height = (int) Math.min(gameWindow.getHeight() * 0.3, 300); // 30% of window height, up to 270px
		// TODO box must be at least 680x200
		x = (int) gameWindow.centerX(); // centered horizontally
		y = gameWindow.getHeight()  - (height / 2 + 10); // centered 10px from bottom of window
		z = Layers.TOP_LAYER - 1;
		// face location and size
		faceWidth = (int) (width * 0.15); // 15% of box width
		faceHeight = (int) (width * 0.15); // square
		faceX = x - width / 2 + (int) (width * 0.02); // 2% from left
		faceY = y - height / 2 + (int) (width * 0.02); // match top pad
		// name position
//		nameX = faceX + faceWidth / 2;
//		nameY = faceY + faceHeight - 2; // 2px overlap with face image
		// line location and spacing
//		lineX = x - (int) (width * 0.3); // 20% from the right
//		lineY = y - (int) (height * 0.45); // 5% from the top
//		float lineWidth = width * 0.78f; // 78% of box width
		// add the arrow
		float arrowX = x + width / 2f - 30;
		float arrowY = y + height / 2f - 15;
		arrow = new Arrow(arrowX, arrowY, 0);
		// PRINT RESULTS
//		Logger.log("*** DIALOGUE DIMENSIONS CALCULATED:\n" +
//				"Box size:     [" + width + " x " + height + "]\n" +
//				"Box location: (" + x + "," + y + ")\n" +
//				"Face size:    [" + faceWidth + " x " + faceHeight + "]\n" +
//				"Text begin:   (" + lineX + ", " + lineY + ")\n" +
//				"Arrow at:     (" + arrowX + ", " + arrowY + ")");
	}

	/**
	 * Advance {@code currentLine} to the next DialogueLine in the array.
	 * @return {@code true} if the next line exists and is a DialogueLine, or
	 * 		   {@code false} if the end of this dialogue sequence has been reached.
	 */
	private boolean nextLine() {
		currentLineIndex++;
		currentChar = 0;
		if (currentLineIndex < lines.length) {
			currentLine = lines[currentLineIndex];
			// check for exit point
			if (currentLine instanceof DialogueLabel) { // if you run into a label you didn't jump to
				return nextLine(); // advance and try again
			}
			else if (currentLine instanceof DialogueJump jump) {
				// jump to the label specified
				return jumpToLabel(jump.getTargetLabel());
			}
			else if (currentLine instanceof DialogueMenu menu) { // if you encounter a menu
				Logger.log("DIALOGUE HAS ENTERED MENU! index = " + currentLineIndex);
				// this is gonna need a special flag
				inMenu = true;
				// activate the menu
//				menu.display();
				// disable other flags for now
				advancing = waiting = false;
				return false;
			}
			if (currentLine.getName().equals("END")) { // if this line is marked as an exit point
				// close this dialogue and return false
				close();
				return false;
			}
			else {
				// otherwise continue as normal
				setTextSettings();
				return true;
			}
		}
		else {
			close();
			return false;
		}
	}

	private boolean jumpToLabel(String targetLabel) {
		Logger.log("Jumping to label: " + targetLabel);
		while (currentLineIndex < lines.length - 1) { // advance the current index
			if (lines[++currentLineIndex] instanceof DialogueLabel label) { // until you find a label
				if (label.getLabel().equals(targetLabel)) { // and if its ID matches
					// found it, so call this method again to properly advance to it
					Logger.log("Label found at index " + currentLineIndex);
					advance();
					return true;
				}
			}
		}
		// if this is reached, then the target does not exist past the point it's called, so give up and close
		Logger.log("Target label not found.");
		return false;
	}

	private void setTextSettings() {
		// number of chars
		lineLength = currentLine.getLine().length();
		// set face and dialogue text
		faceElement.setTexture(currentLine.getFace());
		nameText.replaceText(currentLine.getName());
		// set text and make it all invisible to start
		dialogueText.replaceText(currentLine.getLine());
		dialogueText.setLastCharVisible(0);
		// set the attributes for this line
		if (currentLine.getTextFont() != null) dialogueText.setFont(currentLine.getTextFont());
		else dialogueText.setFont(Game.getFont("dialogue_text"));
		if (currentLine.getNameFont() != null) nameText.setFont(currentLine.getNameFont());
		else nameText.setFont(Game.getFont("dialogue_name"));
		dialogueText.setColor(currentLine.getTextColor());
		nameText.setColor(currentLine.getNameColor());
		boxElement.setColor(currentLine.getBackgroundColor());
		dialogueText.setFontSize(TEXT_FONT_SIZE_DEFAULT * currentLine.getTextSize());
		nameText.setFontSize(NAME_FONT_SIZE_DEFAULT * currentLine.getNameSize());
		charDelay = currentLine.getCharDelay();
	}

	/**
	 * Open the dialogue box. By default it will open on
	 * the first line in the sequence.
	 */
	public void open() {
		Logger.log("Opening dialogue box");
		setLocationAndSize();
		opening = open = true;
		advancing = waiting = closing = false;
		timer = System.nanoTime();
		duration = openTime;
	}

	/**
	 * Open the dialogue box to a specific line in the dialogue
	 * sequence, indexed from 1.
	 * @param line The line to begin on.
	 */
	public void open(int line) {
		opening = true;
		advancing = waiting = closing = false;
		timer = System.nanoTime();
		duration = openTime;
		currentLineIndex = line - 1;
		nextLine();
	}

	/**
	 * Advance to the next dialogue line. Begins the rollout animation
	 * as long as the next line exists.
	 * <br>
	 * In debug mode, this method will check for the shift key being held,
	 * and if it is, the current dialogue sequence will be skipped entirely.
	 */
	public void advance() {
		if (Settings.getSettingBoolean("debug_mode") && KeyInput.isShift()) {
			close();
			return;
		}
		advancing = true;
		opening = waiting = closing = false;
		arrow.reset();
		if (nextLine()) {
			timer = System.nanoTime();
			duration = charDelay;
		}
	}

	/**
	 * Skip the text rollout and display the current line in full immediately.
	 */
	public void skip() {
		if (!currentLine.isAuto()) { // Cannot skip lines flagged as auto-advance
			currentChar = lineLength;
			dialogueText.setLastCharVisible(currentChar);
			waitForNext();
		}
	}

	/**
	 * Close the dialogue box. Begins the closing animation.
	 */
	public void close() {
		Logger.log("Closing dialogue box");
		closing = true;
		opening = advancing = waiting = false;
		timer = System.nanoTime();
		duration = closeTime;
		// remove the text from the screen
		nameText.remove();
		dialogueText.remove();
	}

	// called on input (enter, space, left click)

	/**
	 * This method is called in any valid user dialogue-advance input,
	 * namely a press of the Enter or Space keys, or a left click.
	 * If a menu option is visible nothing happens; if the current
	 * line is still rolling out, it will skip to the end of the rollout
	 * sequence; and if the current line is fully displayed, it will
	 * advance to the next line.
	 */
	public void next() {
		if (inMenu) return; // don't do anything when in the menu
		if (open && !(closing || opening)) {
			if (advancing) skip();
			else advance();
		}
	}

	/**
	 * For testing. Skip this dialogue entirely and
	 * close the dialogue box.
	 */
	public void skipAll() {
		currentLineIndex = lines.length - 1;
		advance();
	}

	// GETTERS
	public boolean isOpen() {
		return open;
	}
	public boolean isOpening() {
		return opening;
	}
	public boolean isWaiting() {
		return waiting;
	}
	public boolean isAdvancing() {
		return advancing;
	}
	public boolean isClosed() { return !(open || opening); }
	public boolean isClosing() {
		return closing;
	}

	// update dialogue box
	public void update() {
		handleInput();
		long elapsed = TimeScale.getScaledTime(timer);
		if (opening) {
			if (elapsed > duration) {
				opening = false;
				advance();
			}
		}
		else if (advancing) {
			if (elapsed > duration) {
				currentChar++;
				if (currentChar > lineLength) {
					if (currentLine.isAuto()) {
						advance();
					}
					else {
						currentChar--; // keep the vertex indices in bounds
						waitForNext();
					}
				}
				else {
					// if the current char is not whitespace, update it
					if ((currentChar < dialogueText.getTextString().length() && dialogueText.getTextString().charAt(currentChar) != ' ')
						|| currentChar == dialogueText.getTextString().length())
						dialogueText.revealNextChar();
					timer = System.nanoTime();
				}
			}
		}
		else if (waiting) {
			arrow.update();
		}
		else if (closing) {
			if (elapsed > duration) {
				closing = open = false;
				currentChar = 0;
				currentLineIndex = -1;
			}
		}
		else if (inMenu) {
			arrow.update();
			DialogueMenu menu = ((DialogueMenu) currentLine);
//			menu.update();
////			Logger.log("Checking for selection...");
//			if (menu.selectionMade()) {
//				inMenu = false;
//				jumpToLabel(menu.getSelection());
//				menu.destroy();
//				currentChar = 0;
//			}
		}

		nameText.update();
		dialogueText.update();
	}

	private void waitForNext() {
		opening = advancing = closing = false;
		waiting = true;
		timer = System.nanoTime();
	}

	// render dialogue box
	public void render() {
		if (opening || closing) {
			// get size
			float tempWidth, tempHeight;
			float percent = TimeScale.getScaledTime(timer) / (float) duration;
			if (closing) percent = 1 - percent;
			tempWidth = width * percent;
			tempHeight = height * percent;
			boxElement.setWidth(tempWidth);
			boxElement.setHeight(tempHeight);
			boxElement.transform();
			// draw box
			Render.drawRect(boxElement);
		}
		else {
			// draw background
			Render.drawRect(boxElement);
			// draw face
			Render.drawImage(faceElement);
			// draw arrow
			if (currentLineIndex < this.lines.length - 1)
				arrow.render();
			if (inMenu) {
//				((DialogueMenu) currentLine).render();
			}
		}

	}

	private void handleInput() {
		if (KeyInput.isPressed(KeyInput.SPACE, KeyInput.ENTER) || MouseInput.leftClick()) {
			if (Settings.getSettingBoolean("debug_mode") && KeyInput.isShift()) skipAll();
			else next();
		}
	}

	public void cleanUp() {
		close();
		dialogueText.remove();
		nameText.remove();
	}

}