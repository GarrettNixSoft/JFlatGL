package com.gnix.jflatgl.core.util.time;

import com.gnix.jflatgl.core.util.Logger;

public class GameThread {

	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			Logger.logError(Logger.MEDIUM, "Sleep error.");
		}
	}

	public static void executeThreaded(GameTask task) {

		GameWorker worker = new GameWorker(task);
		worker.run();

	}

}
