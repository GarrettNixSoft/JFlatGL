package com.floober.engine.gui.event;

import com.floober.engine.util.Logger;
import com.floober.engine.util.time.Timer;

public class BlockingDelayEvent extends GUIEvent {

	private final Timer timer;

	public BlockingDelayEvent(float time) {
		super(true);
		timer = new Timer(time);
	}

	@Override
	public void onStart() {
		Logger.log("Blocking for " + timer.getTime() + "s");
		timer.start();
	}

	@Override
	public void update() {
		if (timer.finished()) complete = true;
	}

	@Override
	public void onFinish() {
		// nothing
	}
}