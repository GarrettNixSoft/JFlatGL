package com.floober.engine.core.assets.loaders;

import org.json.JSONObject;

public abstract class AssetLoader {

	/*
		!! NOTE !!: RECURSIVE mode does not work when exported.
		It is included here for convenience during development.
		You will need to build a JSON directory to point to all
		of your assets when you export the game for distribution.
		Alternatively, you could just always use DIRECTORY mode
		and build your JSON file as you go. (I personally recommend
		this approach)
	 */

	// Modes
	public enum Mode {
		RECURSIVE, DIRECTORY
	}

	protected Mode mode;
	protected JSONObject directory;

	// set the loader's mode (set before calling load()!)
	public void setMode(Mode mode) {
		this.mode = mode;
	}

	// call to execute load procedure during game init phase
	public void load() {
		if (mode == null) {
			throw new IllegalStateException("Load mode not set");
		}
		switch (mode) {
			case RECURSIVE -> loadRecursive();
			case DIRECTORY -> loadDirectory();
		}
	}

	// define these in the individual loaders
	protected abstract void loadRecursive();
	protected abstract void loadDirectory();

}