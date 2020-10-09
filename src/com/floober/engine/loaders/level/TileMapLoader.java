package com.floober.engine.loaders.level;

import com.floober.engine.main.Game;
import com.floober.engine.renderEngine.textures.TextureAtlas;
import com.floober.engine.tiles.TileMap;
import com.floober.engine.util.file.FileUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Byte.parseByte;
import static java.lang.Integer.parseInt;

public class TileMapLoader {

	public static Game game;

	public static TileMap loadTileMap(String file, List<String> extraLinesBuffer) {
		// fail fast: extraLinesBuffer cannot be null
		if (extraLinesBuffer == null)
			throw new RuntimeException("TileMap load error: Extra lines buffer cannot be null!");
		// point to the maps directory
		file = "/level_data/maps/" + file;
		// load the map file data
		ArrayList<String> fileData = FileUtil.getFileData(file);
		Iterator<String> iterator = fileData.iterator();
		// PARSE THE MAP FILE DATA
		// tileset
		String tilesetId = getValue(iterator.next());
		TextureAtlas atlas = game.getTextureAtlas(tilesetId);
		// blocked index
		int blockedIndex = parseInt(getValue(iterator.next()));
		// dimensions
		int rows = parseInt(getValue(iterator.next()));
		int cols = parseInt(getValue(iterator.next()));
		// map data
		byte[][] map = new byte[rows][cols];
		boolean[][] blocked = new boolean[rows][cols];
		for (int r = 0; r < rows; ++r) {
			String[] tokens = iterator.next().split(",");
			for (int c = 0; c < cols; ++c) {
				byte value = parseByte(tokens[c]);
				map[r][c] = value;
				blocked[r][c] = value >= blockedIndex;
			}
		}
		// dump the rest of the lines into the provided list
		while (iterator.hasNext()) {
			extraLinesBuffer.add(iterator.next());
		}
		// create the tilemap
		TileMap tileMap = new TileMap(rows, cols);
		tileMap.setTileset(atlas);
		tileMap.setMap(map);
		tileMap.setBlocked(blocked);
		tileMap.initTileElements();
		// return the result
		return tileMap;
	}

	private static String getValue(String line) {
		return line.substring(line.indexOf('=') + 1);
	}

}