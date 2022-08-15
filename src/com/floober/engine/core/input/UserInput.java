package com.floober.engine.core.input;

import javax.swing.*;

public class UserInput {

	private static JFrame buildParent() {
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		return frame;
	}

	/**
	 * Prompt the user to enter a String of input.
	 * @param prompt the prompt message to show the user
	 * @return the user's input as a String
	 */
	public static String getUserString(String prompt) {
		JFrame frame = buildParent();
		String result = JOptionPane.showInputDialog(frame, prompt);
		frame.dispose();
		return result;
	}

	/**
	 * Prompt the user to enter an integer as input.
	 * Will repeat each time the user enters a value that
	 * causes {@code Integer.parseInt()} to fail. If the
	 * user chooses to cancel, 0 will be returned.
	 * @param prompt the prompt message to show the user
	 * @return the user's input as an int
	 */
	public static int getUserInt(String prompt) {
		while (true) {
			String response = getUserString(prompt);

			if (response == null) return 0;

			try {
				return Integer.parseInt(response);
			}
			catch (NumberFormatException e) {
				showErrorMessage("Please enter a valid integer.", "Invalid Input");
			}
		}
	}

	public static void showErrorMessage(String message, String title) {
		JFrame frame = buildParent();
		JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
		frame.dispose();
	}

}
