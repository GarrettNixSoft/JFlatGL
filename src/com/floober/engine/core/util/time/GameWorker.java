package com.floober.engine.core.util.time;

public class GameWorker implements Runnable {

	private final GameTask task;

	public GameWorker(GameTask task) {
		this.task = task;
	}

	@Override
	public void run() {
		task.run();
	}

}
