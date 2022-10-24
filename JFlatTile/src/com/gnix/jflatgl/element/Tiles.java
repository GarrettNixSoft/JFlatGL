package com.gnix.jflatgl.element;

import com.gnix.jflatgl.core.entity.EntityActor;

import java.util.HashMap;

public class Tiles {

	public static final HashMap<Integer, TileSettings> mappedTileValues = new HashMap<>();
	public static final HashMap<Integer, EntityActor> tileActions = new HashMap<>();

	static {
		// DEFAULT TILE
		mappedTileValues.put(-1, new TileSettings(-1, 0, true, false, null, null, null, null, false, null, 0, 0));
	}

}
