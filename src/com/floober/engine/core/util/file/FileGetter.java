package com.floober.engine.core.util.file;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileGetter {

	public static File getFile(String startingDir, String... extensions) {

		JFileChooser fileChooser = new JFileChooser(startingDir);

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				for (String ext : extensions) {
					if (f.getName().toLowerCase().endsWith(ext)) return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return "Accepts files with extensions passed to the FileGetter.getFile() method";
			}
		};

		fileChooser.addChoosableFileFilter(fileFilter);

		int selection = fileChooser.showOpenDialog(null);

		if (selection == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();

		else if (selection == JFileChooser.CANCEL_OPTION)
			return null;

		else return null;

	}

}
