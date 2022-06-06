package com.floober.engine.core.renderEngine.elements.tile;

import com.floober.engine.core.assets.loaders.Loader;
import com.floober.engine.core.entity.EntityActor;
import com.floober.engine.core.renderEngine.textures.TextureComponent;
import com.floober.engine.core.util.color.Colors;
import org.joml.Vector4f;

import java.util.HashMap;

public class Tiles {

	public static final HashMap<Integer, TileSettings> mappedTileValues = new HashMap<>();
	public static final HashMap<Integer, EntityActor> tileActions = new HashMap<>();

	static {
		// DEFAULT TILE
		mappedTileValues.put(-1, new TileSettings(-1, 0, true, false, null, null, null, null, false, null, 0, 0));
		// BOUNCE TILES
		TextureComponent bounceOverlay = Loader.loadTexture("tilesets/effects/arrow_glow.png");
		mappedTileValues.put(42, new TileSettings(42, 0, false, true, Colors.BLACK, Colors.CYAN, Colors.GOLD, new Vector4f(0), true, bounceOverlay, 0.5f, -0));
		mappedTileValues.put(43, new TileSettings(42, -90, false, true, Colors.BLACK, Colors.CYAN, Colors.GOLD, new Vector4f(0), true, bounceOverlay, 0.5f, -90));
		mappedTileValues.put(44, new TileSettings(42, -180, false, true, Colors.BLACK, Colors.CYAN, Colors.GOLD, new Vector4f(0), true, bounceOverlay, 0.5f, -180));
		mappedTileValues.put(45, new TileSettings(42, -270, false, true, Colors.BLACK, Colors.CYAN, Colors.GOLD, new Vector4f(0), true, bounceOverlay, 0.5f, -270));
		// BOUNCE TILE ACTIONS
		tileActions.put(42, e -> e.setYVel(-1000));
		tileActions.put(43, e -> e.setXVel(1000));
		tileActions.put(44, e -> e.setYVel(1000));
		tileActions.put(45, e -> e.setXVel(-1000));
	}

}
