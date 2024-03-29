package com.gnix.jflatgl.core.util.file;

import com.gnix.jflatgl.core.util.configuration.Config;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileGetter {

	static {
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ignored) {}
	}

	public static File getFile(String startingDir, String filterDesc, String... extensions) {
		return promptForFile(false, startingDir, filterDesc, extensions);
	}

	public static File getSaveDestination(String startingDir, String filterDesc, String... extensions) {
		return promptForFile(true, startingDir, filterDesc, extensions);
	}

	private static File promptForFile(boolean save, String startingDir, String filterDesc, String... extensions) {

		JFileChooser fileChooser = new JFileChooser(startingDir);

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File f) {
				for (String ext : extensions) {
					if (f.getName().toLowerCase().endsWith(ext)) return true;
					else if (f.isDirectory()) return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return filterDesc;
			}
		};

//		fileChooser.addChoosableFileFilter(fileFilter);
		fileChooser.setFileFilter(fileFilter);

		JFrame parent = new JFrame("Choose a Map File");
		parent.setIconImage(Config.ICON_IMAGE);
		parent.setAlwaysOnTop(true);

		int selection = save ? fileChooser.showSaveDialog(parent) : fileChooser.showOpenDialog(parent);
		File selectedFile = fileChooser.getSelectedFile();

		parent.dispose();

		if (selection == JFileChooser.APPROVE_OPTION)
			return selectedFile;

		else if (selection == JFileChooser.CANCEL_OPTION)
			return null;

		else return null;

	}

	public static File getDirectory(String startingDir) {

		JFileChooser fileChooser = new JFileChooser(startingDir);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JFrame parent = new JFrame("Choose a Folder");
		parent.setIconImage(Config.ICON_IMAGE);
		parent.setAlwaysOnTop(true);

		int selection = fileChooser.showOpenDialog(parent);
		File selectedFile = fileChooser.getSelectedFile();

		parent.dispose();

		if (selection == JFileChooser.APPROVE_OPTION)
			return selectedFile;

		else if (selection == JFileChooser.CANCEL_OPTION)
			return null;

		else return null;

	}

}
