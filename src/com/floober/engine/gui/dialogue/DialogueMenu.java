package com.floober.engine.gui.dialogue;

import com.floober.engine.game.Game;
import org.json.JSONArray;

public class DialogueMenu extends DialogueLine {

	// TODO reimplement with new GUI library

//	private final GUI buttonGUI;
//	private final List<DialogueButton> buttons = new ArrayList<>();
//
//	private final int buttonHeight = 80;

	public DialogueMenu(JSONArray options) {
		super(null, null, null);
//		buttonGUI = new GUI(game);
//		buttonGUI.popLayer("background");
//		initMenuButtons(options);
	}

	private void initMenuButtons(JSONArray options) {
//		// create the GUI layer
//		GUILayer layer = new GUILayer("dialogue_options");
//		Logger.log("layer added. ID = " + layer.getId());
//		// continue if valid
//		int length = options.length();
//		int totalSpace = 0;
//		int[] sizes = new int[length];
//		for (int i = 0; i < length; i++) {
//			DialogueButton button = createButton(options.getJSONObject(i));
//			totalSpace += sizes[i] = getButtonSize(button.getGUIText().getNumberOfLines(), i, length);
//			buttons.add(button);
//		}
//		// position the buttons and add all buttons to the layer
//		int spaceCovered = 0;
//		for (int i = 0; i < length; i++) {
//			DialogueButton button = buttons.get(i);
//			// position it
//			int y = getYPosition(totalSpace, spaceCovered, sizes[i]);
//			float screenY = (float) y / Config.DEFAULT_RESOLUTION_HEIGHT;
////			screenY = screenY * 2 - 1f;
//			Vector3f position = new Vector3f(0.5f, screenY, 0);
//			button.setPosition(position, y);
//			// add it to the layer
//			layer.addElement(button);
//			// add up the space covered
//			spaceCovered += getButtonSize(button.getGUIText().getNumberOfLines(), i, length);
//		}
//		// add the layer
//		buttonGUI.addLayer(layer);
	}

//	private DialogueButton createButton(JSONObject json) {
//		// get JSON data
//		String text = json.getString("text");
//		String label = json.getString("label");
//		Vector4f textColor = ColorConverter.getColorByName(json.optString("textColor", "white"));
//		Vector4f textHoverColor = ColorConverter.getColorByName(json.optString("textHoverColor", "yellow"));
//		String fontName = json.optString("font", "dialogue_text");
//		// fetch the game
//		Game game = buttonGUI.getGame();
//		// create the GUI text
//		GUIText guiText = new GUIText(text, 1.5f, game.getFont(fontName), new Vector3f(0), 0.6f, true);
//		guiText.setColor(textColor);
//		guiText.setWidth(0.5f);
//		guiText.setEdge(0.2f);
//		guiText.setOutlineColor(Colors.BLACK);
//		guiText.setBorderWidth(0.6f);
//		guiText.setBorderEdge(0.1f);
//		// create the button
//		DialogueButton button = new DialogueButton("dialogue_option_" + label, game, buttonGUI, guiText, buttonHeight, textColor, textHoverColor, label);
//		// create the button's click behavior
//		button.setMouseOverBehavior(game1 -> game1.playSfx("button_hover"));
//		button.setLeftClickBehavior(game1 -> {
//			game1.playSfx("button_click");
//			button.flagSelected();
//		});
//		// return the finished button
//		return button;
//	}

//	private int getButtonSize(int numLines, int index, int total) {
//		int extraLineSize = 50;
//		int spacing = 50;
//		// size of the box, plus spacing if it's not the last one
//		return buttonHeight + (--numLines * extraLineSize) + (index < total ? spacing : 0);
//	}
//
//	private int getYPosition(int totalSpace, int spaceCovered, int size) {
//		int center = (int) (Config.DEFAULT_RESOLUTION_HEIGHT * 0.35);
//		return center - (totalSpace / 2) + spaceCovered + (size / 2);
//	}
//
//	/**
//	 * Update this DialogueMenu.
//	 */
//	public void update() {
//		buttonGUI.update();
//	}
//
//	/**
//	 * Render this DialogueMenu.
//	 */
//	public void render() {
//		buttonGUI.render();
//	}
//
//	/**
//	 * Check if the player has made a selection.
//	 * @return {@code true} if any buttons have been selected.
//	 */
//	public boolean selectionMade() {
//		for (DialogueButton button : buttons) if (button.isSelected()) return true;
//		return false;
//	}
//
//	/**
//	 * Get the label that corresponds to the player's choice.
//	 * @return The label, or an empty string if no selection has been made.
//	 */
//	public String getSelection() {
//		for (DialogueButton button : buttons) if (button.isSelected()) return button.getTargetLabel();
//		return "";
//	}
//
//	/**
//	 * Display the menu buttons and text on the screen.
//	 */
//	public void display() {
//		buttonGUI.getTopLayer().enableLayer();
//		buttonGUI.getTopLayer().setAllVisible(true);
//		for (DialogueButton button : buttons) button.display();
//	}
//
//	/**
//	 * Clear the menu buttons and text from the screen.
//	 */
//	public void destroy() {
//		buttonGUI.getTopLayer().setAllVisible(false);
//		buttonGUI.getTopLayer().disableLayer();
//		for (DialogueButton button : buttons) button.destroy();
//	}

}