package com.floober.engine.util.file;

import com.floober.engine.util.Logger;

import java.io.*;
import java.util.ArrayList;

public class FileUtil {
	
	public static ArrayList<String> getFileData(String path) {
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

	public static File getFile(String path) {
		path = path.replace("\\", "/");
		File file = new File(path);
		if (!file.exists()) {
			file = new File("/" + path);
			if (!file.exists())
				file = new File("res/" + path);
				if (!file.exists()) {
					file = new File("/res/" + path);
					if (!file.exists())
						throw new RuntimeException("File " + path + " couldn't be found.");
			}
		}
		return file;
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
	
}