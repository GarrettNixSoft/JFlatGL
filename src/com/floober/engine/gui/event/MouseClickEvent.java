package com.floober.engine.gui.event;

public record MouseClickEvent(
		int button,
		int x,
		int y
) {}
