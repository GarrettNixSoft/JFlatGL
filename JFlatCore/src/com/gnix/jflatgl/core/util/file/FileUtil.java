package com.gnix.jflatgl.core.util.file;

import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.core.util.StringUtils;
import com.gnix.jflatgl.core.util.configuration.Config;
import com.gnix.jflatgl.core.util.conversion.StringConverter;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static final String SEPARATOR;
	public static final String GAME_DIR;

	static {
		String sep = System.getProperty("file.separator");
		System.out.println("Separator = " + sep);
		SEPARATOR = "/";
		GAME_DIR = System.getProperty("user.home") + SEPARATOR + "Games" + FileUtil.SEPARATOR + Config.GAME_TITLE_ON_DISK;
	}

	public static String generateFilePath(String... directories) {
		StringBuilder builder = new StringBuilder();
		builder.append(GAME_DIR).append(SEPARATOR);
		for (String directory : directories) builder.append(directory).append(SEPARATOR);
		return builder.toString();
	}

	public static ArrayList<String> getFileDataDirectly(String path) {
		path = path.replace("\\", "/");
		Logger.logLoad("Loading file: " + path);
		ArrayList<String> data = new ArrayList<>();
		try {
			InputStream in = FileUtil.class.getResourceAsStream(path);
			if (in == null)
				in = ResourceLoader.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				data.add(line);
			}
			reader.close();
		} catch (Exception e) {
			Logger.logLoadError("Failed to load file: " + path);
			e.printStackTrace();
			return data;
		}
		return data;
	}

	public static ArrayList<String> getFileData(String path) {
		path = path.replace("\\", "/");
		File file = getFile(path);
		return getFileData(file);
	}

	public static ArrayList<String> getFileData(File file) {
		Logger.logLoad("Loading file: " + file.getName());
		ArrayList<String> data = new ArrayList<>();
		try {
			InputStream in = FileUtil.class.getResourceAsStream(file.getPath());
			if (in == null)
				in = ResourceLoader.getResourceAsStream(file.getPath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while (true) {
				String line = reader.readLine();
				if (line == null) break;
				data.add(line);
			}
			reader.close();
		} catch (Exception e) {
			Logger.logLoadError("Failed to load file: " + file.getPath());
//			e.printStackTrace();
			return data;
		}
		return data;
	}

	public static ArrayList<String> getResourceDataFile(String path) {
		path = path.replace("\\", "/");
		File file = new File("resourceData/" + path);
		try {
			ArrayList<String> data = getFileData(file);
			if (data.isEmpty() || (data.size() == 1 && data.get(0).isEmpty())) throw new RuntimeException();
			else return data;
		} catch (Exception e) {
			file = new File("/resourceData/" + path);
			return getFileData(file);
		}
	}

	public static ArrayList<String> getOrCreateResourceDataFile(String path, JSONObject defaultData) {
		path = path.replace("\\", "/");
		File file = new File("resourceData/" + path);
		if (file.exists()) {
			try {
				ArrayList<String> data = getFileData(file);
				if (data.isEmpty() || (data.size() == 1 && data.get(0).isEmpty())) throw new RuntimeException();
				else return data;
			} catch (Exception e) {
				file = new File("/resourceData/" + path);
				return getFileData(file);
			}
		}
		else {
			file.getParentFile().mkdirs();
			if (!(file.getParentFile().exists() && file.getParentFile().isDirectory())) throw new RuntimeException("Failed to create directory tree for " + path);
			else Logger.logWarning("Created directory " + file.getParent());
			try {
				if (!file.createNewFile()) throw new RuntimeException("Could not create " + path);
				else Logger.logWarning("Created " + path);
				writeJSON(defaultData, file.getPath());
				Logger.logWarning("Wrote default data to disk: " + defaultData.toString(4));
				return getResourceDataFile(path);
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	}

	public static ArrayList<String> getResDataFile(String path) {
		path = path.replace("\\", "/");
		File file = new File("res/" + path);
		try {
			ArrayList<String> data = getFileData(file);
			if (data.isEmpty() || (data.size() == 1 && data.get(0).isEmpty())) throw new RuntimeException();
			else return data;
		} catch (Exception e) {
			file = new File("/res/" + path);
			return getFileData(file);
		}
	}

	public static File getFile(String filePath) {
		filePath = filePath.replace("\\", "/");
		Path path = Paths.get(filePath);
		if (!path.toFile().exists()) {
			path = Paths.get("/" + filePath);
			if (!path.toFile().exists()) {
				path = Paths.get("res/" + filePath);
				if (!path.toFile().exists()) {
					path = Paths.get("/res/" + filePath);
					if (!path.toFile().exists()) {
						path = Paths.get("resourceData/" + filePath);
						if (!path.toFile().exists()) {
							path = Paths.get("/resourceData/" + filePath);
							if (!path.toFile().exists())
								throw new RuntimeException("File " + filePath + " couldn't be found.");
						} else {
							System.out.println("Succeeded at: " + path);
						}
					} else {
						System.out.println("Succeeded at: " + path);
					}
				} else {
					System.out.println("Succeeded at: " + path);
				}
			} else {
				System.out.println("Succeeded at: " + path);
			}
		}
		return path.toFile();
	}

	public static void writeFile(String path, String name, String extension, ArrayList<String> data) {
		try {
			File target = new File(path + "\\" + name + "." + extension);
			FileWriter fw = new FileWriter(target);
			BufferedWriter writer = new BufferedWriter(fw);
			for (String line : data) {
				writer.write(line);
				writer.write(System.lineSeparator());
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean writeFile(File file, byte[] bytes) {

		try (FileOutputStream stream = new FileOutputStream(file)) {

			stream.write(bytes);
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	// LOAD METHODS
	public static byte[] getFileBytes(String filePath) {
		try {
			InputStream in = FileUtil.class.getResourceAsStream(filePath);
			if (in == null)
				in = ResourceLoader.getResourceAsStream(filePath);
			return in.readAllBytes();
		}
		catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static byte[] getFileBytes(File file) {
		try (FileInputStream stream = new FileInputStream(file)) {
			return stream.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static JSONObject getJSON(File file) {
		String combinedData = StringConverter.combineAll(getFileData(file));
		return new JSONObject(combinedData);
	}

	public static JSONObject getJSON(String file) {
		// load file data
//		Logger.log("Getting JSON: " + file);
		ArrayList<String> fileData =
				file.contains("assets") || file.contains("backgrounds") || file.endsWith("config.json") || file.endsWith("settings.json") ?
					getResourceDataFile(file) :
						StringUtils.containsAny(file, ".fnt", ".png", ".wav") ?
							getResDataFile(file) :
							getFileData(file);
//		Logger.log("Got JSON: " + file);
		String combined = StringConverter.combineAll(fileData);
//		System.out.println(combined);
		// create JSON parser
		return new JSONObject(combined);
	}

	public static JSONObject getOrCreateJSON(String file) {
		// Attempt to load the JSON like usual
		try {
			return getJSON(file);
		} catch (Exception e) {
			// If that fails, attempt to create the file if it does not exist
			try {
				File fileOnDisk = new File(file);
				if (!fileOnDisk.exists()) {
					File dir = fileOnDisk.getParentFile();
					boolean success = dir.mkdirs();
					// If the mkdirs call failed or the file can't be created, error out
					if (!success || !fileOnDisk.createNewFile()) throw new IOException("Failed to create file");
				}
				// otherwise, create a new blank JSON which will be written to that file later
				return new JSONObject("{}");
			} catch (Exception e2) {
				Logger.logError("Could not create TextureAnalyzer data cache file!", Logger.HIGH, e2);
				return null;
			}
		}
	}

	public static JSONObject getResourceDataJSON(String path) {
		ArrayList<String> fileData = getResourceDataFile(path);
		String combined = StringConverter.combineAll(fileData);
		return new JSONObject(combined);
	}

	public static JSONObject getOrCreateResourceDataJSON(String path, JSONObject defaultData) {
		ArrayList<String> fileData = getOrCreateResourceDataFile(path, defaultData);
		String combined = StringConverter.combineAll(fileData);
		if (combined.isEmpty()) return new JSONObject();
		else return new JSONObject(combined);
	}

	public static String[] getFileRaw(String path) {
		try {
			InputStream in = ResourceLoader.getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			List<String> lines = new ArrayList<>();
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			return lines.toArray(new String[0]);
		} catch (Exception e) {
			Logger.logError(e.toString());
			return null;
		}
	}

	/**
	 * Write all of the data in the given {@code JSONObject} into a file
	 * at the path specified by the {@code file} String.
	 * @param json the {@code JSONObject} to write to disk
	 * @param file the path for the desired file
	 */
	public static void writeJSON(JSONObject json, String file) {
		try {
			File target = new File(file);
			if (!target.exists()) {
				if (target.createNewFile()) {
					FileWriter fw = new FileWriter(target);
					BufferedWriter writer = new BufferedWriter(fw);
					String jsonStr = json.toString(4);
//					Logger.log("JSON to write to file: " + jsonStr);
					String[] data = jsonStr.split("\n");
					for (String line : data) {
						writer.write(line);
						writer.write(System.lineSeparator());
					}
					writer.close();
				}
				else {
					String directoryPath = file.substring(0, file.lastIndexOf(SEPARATOR));
					File directory = new File(directoryPath);
					if (directory.mkdirs()) {
						if (target.createNewFile()) {
							Logger.log("Created new file: " + file);
							FileWriter fw = new FileWriter(target);
							BufferedWriter writer = new BufferedWriter(fw);
							String jsonStr = json.toString(4);
//							Logger.log("JSON to write to file: " + jsonStr);
							String[] data = jsonStr.split("\n");
							for (String line : data) {
								writer.write(line);
								writer.write(System.lineSeparator());
							}
							writer.close();
						} else throw new RuntimeException("File " + file + " did not exist and could not be created");
					}
					else throw new RuntimeException("File " + file + " did not exist and could not be created");
				}
			}
			else {
				FileWriter fw = new FileWriter(target);
				BufferedWriter writer = new BufferedWriter(fw);
				String jsonStr = json.toString(4);
//				Logger.log("JSON to write to file: " + jsonStr);
				String[] data = jsonStr.split("\n");
				for (String line : data) {
					writer.write(line);
					writer.write(System.lineSeparator());
				}
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
