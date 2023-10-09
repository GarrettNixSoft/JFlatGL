package com.gnix.jflatgl.core.input;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.configuration.Config;
import com.gnix.jflatgl.core.util.file.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControlSchemeParser {

	public static Map<String, ControlContext> loadControls() {

		// determine where the control configuration files should be stored
		String controlsDirPath = System.getProperty("user.dir") + Config.CONTROLS_PATH;

		Logger.logLoadControls("Controls directory: " + controlsDirPath);

		// get that directory
		File controlsDir = Paths.get(controlsDirPath).toFile();

		// if it does not exist, it hasn't been initialized yet; do so, and return the default controls
		if (!controlsDir.exists()) {
			return initControlsFiles(controlsDir);
		}

		// otherwise, parse the control files
		return parseControlsFiles(controlsDir);

	}

	private static Map<String, ControlContext> initControlsFiles(File controlsDir) {

		Logger.logLoadControls("No controls directory found; loading defaults");

		// create the directory
		boolean created = controlsDir.mkdirs();

		if (!created) throw new RuntimeException("Failed to create controls directory: " + controlsDir.getPath());

		// prepare a list to store the results
		Map<String, ControlContext> result = new HashMap<>();

		// load the default controls pointed to in the config
		String templateLocation = Config.DEFAULT_CONTROLS_DIR;

		// check whether a "contexts" entry exists
		try {

			JSONObject contextsJSON = FileUtil.getOrCreateResourceDataJSON(templateLocation + "/contexts.json", new JSONObject());
			List<String> contexts = parseContextsFile(contextsJSON);

			for (String context : contexts) {

				JSONObject contextJSON = FileUtil.getResourceDataJSON(templateLocation + "/" + context + ".json");
				ControlContext controls = parseControlsFile(context, contextJSON);
				result.put(context, controls);

			}

		}
		catch (Exception e) {

			Logger.log("No context list found; loading single control list");

			// get the single controls file
			JSONObject controlsJSON = FileUtil.getOrCreateResourceDataJSON(templateLocation + "/controls.json", new JSONObject());
			result.put("controls", parseControlsFile("controls", controlsJSON));

		}

		// save the controls to copy them to the controls directory
		saveControls(result);

		// return the result when finished
		return result;

	}

	private static Map<String, ControlContext> parseControlsFiles(File controlsDir) {

		// load the contexts list
		String controlsDirPath = controlsDir.getPath();
		String contextsPath = controlsDirPath + "/contexts.json";
		File contextsFile = Paths.get(contextsPath).toFile();

		// if the contexts file exists, load the multi-context controls
		if (contextsFile.exists()) {
			return loadMultiContextControls(contextsFile, controlsDirPath);
		}

		// otherwise, load a single control scheme
		else {
			String controlsPath = controlsDirPath + "/controls.json";
			File controlsFile = Paths.get(controlsPath).toFile();
			JSONObject controlsFileJSON = FileUtil.getJSON(controlsFile);
			String contextName = controlsFile.getName().replace(".json", "");
			return loadSingleControlScheme(contextName, controlsFileJSON);
		}

	}

	private static Map<String, ControlContext> loadMultiContextControls(File contextsFile, String controlsDirPath) {

		Logger.logLoadControls("Context list found! Loading multiple control contexts...");

		JSONObject contextsFileJSON = FileUtil.getJSON(contextsFile);
		List<String> contexts = parseContextsFile(contextsFileJSON);

		// load the control context for each entry
		Map<String, ControlContext> result = new HashMap<>();

		for (String context : contexts) {

			String contextPath = controlsDirPath + "/" + context + ".json";
			File controlsFile = Paths.get(contextPath).toFile();
			JSONObject controlsFileJSON = FileUtil.getJSON(contextsFile);
			String contextName = controlsFile.getName().replace(".json", "");
			result.put(context, parseControlsFile(contextName, controlsFileJSON));

		}

		// return when finished
		return result;

	}

	private static Map<String, ControlContext> loadSingleControlScheme(String contextName, JSONObject controlsFile) {

		Logger.logLoadControls("No context list found! Loading single control context...");

		Map<String, ControlContext> result = new HashMap<>();

		// load the single file
		ControlContext controls = parseControlsFile(contextName, controlsFile);
		result.put(contextName, controls);

		return result;

	}

	private static List<String> parseContextsFile(JSONObject contextsFile) {

		Logger.logLoadControls("Parsing contexts file...");

		JSONArray contextsArray = contextsFile.getJSONArray("contexts");

		List<String> contexts = new ArrayList<>();
		for (int i = 0; i < contextsArray.length(); i++) {
			String context = contextsArray.getString(i);
			contexts.add(context);
			Logger.logLoadControls("Found context: " + context);
		}

		return contexts;

	}

	private static ControlContext parseControlsFile(String contextName, JSONObject controlsFile) {

		Logger.logLoadControls("Parsing controls file for context: " + contextName);

		// create the context
		ControlContext context = new ControlContext(contextName);

		// parse every control entry
		for (String control : controlsFile.keySet()) {
			Keybind keybind = new Keybind(contextName, control, controlsFile.getJSONObject(control));
			context.addKeybind(keybind);
			Logger.logLoadControls("Loaded binds for control: " + control);
		}

		// return when finished
		return context;

	}

	public static void saveControls(Map<String, ControlContext> controlContexts) {

		// find where to save the controls
		String controlsDirPath = System.getProperty("user.dir") + Config.CONTROLS_PATH;

		Logger.logLoadControls("Saving controls to: " + controlsDirPath);
		Logger.logLoadControls("Found " + controlContexts.size() + " contexts to save");

		// save them all
		for (String contextName : controlContexts.keySet()) {

			// get the file target
			String path = controlsDirPath + "/" + contextName + ".json";

			Logger.logLoadControls("Saving controls file:" + path);

			// get the JSON to store
			ControlContext controlContext = controlContexts.get(contextName);
			JSONObject controlJSON = buildJSON(controlContext);

			// write it to the file
			FileUtil.writeJSON(controlJSON, path);

		}


	}

	private static JSONObject buildJSON(ControlContext controlContext) {

		// create an empty JSON object
		JSONObject result = new JSONObject();

		// add all the controls to it
		Map<String, Keybind> controls = controlContext.getKeybinds();

		for (String control : controls.keySet()) {
			Keybind keybind = controls.get(control);
			result.put(control, keybind.getJSON());
		}

		// return when finished
		return result;

	}

}
