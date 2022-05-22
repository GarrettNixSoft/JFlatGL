package com.floober.engine.gui.dialogue;

import com.floober.engine.core.Game;
import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.Logger;
import com.floober.engine.core.util.color.ColorConverter;
import com.floober.engine.core.util.color.Colors;
import com.floober.engine.core.util.configuration.Settings;
import com.floober.engine.core.util.file.FileUtil;
import org.joml.Vector4f;
import org.json.JSONObject;

public class DialogueLoader {

	private static final JSONObject nameMap;
	private static final JSONObject faceMap;

	static {
		nameMap = FileUtil.getJSON("/level_data/dialogue/maps/name_map.json");
		faceMap = FileUtil.getJSON("/level_data/dialogue/maps/face_map.json");
	}

	public static Dialogue load(String path) {
		// get file
		String[] lines = FileUtil.getFileRaw("level_data/dialogue/" + path);
		// first line is for settings
		assert lines != null;
		// load lines and return result
		return parseLines(lines, parseSettings(lines[0]));
	}

	private static Dialogue parseLines(String[] lines, DialogueConfiguration configuration) {
		// allocate array of lines
		DialogueLine[] dialogueLines = new DialogueLine[lines.length - 1];
		// parse the lines
		for (int i = 0; i < lines.length - 1; ++i) {
			String rawLine = lines[i + 1];
			if (rawLine.startsWith("//")) continue; // allows for comments in dialogue files
			else if (rawLine.trim().isEmpty()) continue; // allows for blank lines
			dialogueLines[i] = parseLine(rawLine);
		}
		Logger.logLoad("Loaded " + (lines.length - 1) + " lines");
		// put lines into the thing
		return new Dialogue(dialogueLines, configuration);
	}

	private static DialogueLine parseLine(String rawLine) {
//		Logger.log("Parsing line: " + rawLine);
		// all variables that need to be initialized before construction, with some default values
		TextureComponent face;
		String name, line;
		FontType textFont = null, nameFont = null;
		Vector4f textColor = new Vector4f(Colors.WHITE), nameColor = new Vector4f(Colors.WHITE), backgroundColor = new Vector4f(0.05f, 0.05f, 0.05f, 0.5f);
		float textSize = 1, nameSize = 1;
		int charDelay = Settings.dialogueCharDelay;
		boolean auto = false;
		// check for line data in JSON format at the start of the line
		if (rawLine.startsWith("{")) {
			// get all the JSON data
			String rawJSON = rawLine.substring(0, rawLine.indexOf("} ") + 1);
			JSONObject json = new JSONObject(rawJSON);
			// get the font for the dialogue text
			String textFontName = json.optString("textFont");
			if (textFontName != null) textFont = Game.getFont(textFontName);
			// get the font for the name text
			String nameFontName = json.optString("nameFont");
			if (nameFontName != null) nameFont = Game.getFont(nameFontName);
			// get the dialogue text color
			String textColorName = json.optString("textColor", null);
			if (textColorName != null) textColor = ColorConverter.getColorByName(textColorName);
			// get the name text color
			String nameColorName = json.optString("nameColor", null);
			if (nameColorName != null) nameColor = ColorConverter.getColorByName(nameColorName);
			String backgroundColorName = json.optString("backgroundColor", null);
			if (backgroundColorName != null) backgroundColor = ColorConverter.getColorByName(backgroundColorName);
			// get the dialogue text and name text sizes
			textSize = json.optFloat("textSize", 1);
			nameSize = json.optFloat("nameSize", 1);
			// get the character advance delay
			charDelay = json.optInt("charDelay", Settings.dialogueCharDelay);
			// get the auto-advance flag
			auto = json.optBoolean("auto", false);
			// trim off the JSON data for the rest of the parsing process
			rawLine = rawLine.substring(rawLine.indexOf("} ") + 2);
		}
		// else check for jump point
		else if (rawLine.startsWith("*jump")) {
			String jumpLabel = rawLine.substring(rawLine.indexOf(" ") + 1);
			return new DialogueJump(jumpLabel);
		}
		// else check for label
		else if (rawLine.startsWith("*label")) {
			String label = rawLine.substring(rawLine.indexOf(" ") + 1);
			return new DialogueLabel(label);
		}
		// else check for a dialogue option menu
		else if (rawLine.startsWith("*menu")) {
			String rawJSON = rawLine.substring(rawLine.indexOf('{')).trim();
			JSONObject json = new JSONObject(rawJSON);
			return new DialogueMenu(json.getJSONArray("options"));
		}
		else if (rawLine.startsWith("*event")) {
			String rawJSON = rawLine.substring(rawLine.indexOf('{')).trim();
			JSONObject json = new JSONObject(rawJSON);
			String type = json.getString("type");
			switch (type) {
				case "audio":
					// TODO load an audio event
					break;
				case "ui":
					// TODO load a GUI event
					break;
				case "cutscene":
					// TODO load a cutscene event
					break;
				default:
					Logger.logError("Dialogue event specified an unknown type: " + type, Logger.LOW);
					break;
			}
		}
		// get lines
		int firstSpace = rawLine.indexOf(" ");
		String identity = rawLine.substring(0, firstSpace);
		String rest = rawLine.substring(firstSpace);
		line = rest.substring(2, rest.lastIndexOf("\""));
		// get identity
		String faceId = faceMap.getString(identity);
		name = nameMap.getString(identity);
		// retrieve face tex
		face = Game.getTexture(faceId);
		// TODO add more code to parse extra tokens (can be affected by settings values from first line)
		// create and return line
		DialogueLine result = new DialogueLine(face, name, line);
		// charDelay, auto, jumpLabel
		result.setNameFont(nameFont);
		result.setTextFont(textFont);
		result.setNameColor(nameColor);
		result.setTextColor(textColor);
		result.setBackgroundColor(backgroundColor);
		result.setNameSize(nameSize);
		result.setTextSize(textSize);
		result.setCharDelay(charDelay);
		result.setAuto(auto);
		return result;
	}

	private static DialogueConfiguration parseSettings(String settingsLine) {
		String[] tokens = settingsLine.split(" ");
		if (!tokens[1].equals("default")) {
			// get JSON data
			JSONObject json = new JSONObject(tokens[1]);
			// set text font type if it's defined
			FontType textFont = null, nameFont = null;
			String textFontName = json.optString("textFont");
			if (textFontName != null) textFont = Game.getFont(textFontName);
			String nameFontName = json.optString("nameFont");
			if (nameFontName != null) nameFont = Game.getFont(nameFontName);
			// set color if it's defined
			Vector4f textColor = new Vector4f(Colors.WHITE), nameColor = new Vector4f(Colors.WHITE), backgroundColor = new Vector4f(0.05f, 0.05f, 0.05f, 0.5f);
			String textColorName = json.optString("textColor", null);
			if (textColorName != null) textColor = ColorConverter.getColorByName(textColorName);
			String nameColorName = json.optString("nameColor", null);
			if (nameColorName != null) nameColor = ColorConverter.getColorByName(nameColorName);
			String backgroundColorName = json.optString("backgroundColor", null);
			if (backgroundColorName != null) backgroundColor = ColorConverter.getColorByName(backgroundColorName);
			// set size if it's defined
			float textSize = json.optFloat("textSize", 1);
			float nameSize = json.optFloat("nameSize", 1);
			// return results
			return new DialogueConfiguration(textFont, nameFont, textColor, nameColor, backgroundColor, textSize, nameSize);
		}
		else {
			return new DialogueConfiguration(Game.getFont("dialogue_text"), Game.getFont("dialogue_name"),
					new Vector4f(Colors.WHITE), new Vector4f(Colors.WHITE), new Vector4f(0.05f, 0.05f, 0.05f, 0.5f),
					1, 1);
		}
	}
}
