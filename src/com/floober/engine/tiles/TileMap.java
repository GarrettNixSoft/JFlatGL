package com.floober.engine.tiles;

import com.floober.engine.display.Display;
import com.floober.engine.display.DisplayManager;
import com.floober.engine.renderEngine.MasterRenderer;
import com.floober.engine.renderEngine.Render;
import com.floober.engine.renderEngine.elements.TileElement;
import com.floober.engine.textures.Texture;
import com.floober.engine.textures.TextureAtlas;
import com.floober.engine.util.Logger;
import com.floober.engine.util.data.Config;
import org.joml.Vector2f;

public class TileMap {

	// raw map data
	private byte[][] map;
	private boolean[][] blocked;
	private final int numRows;
	private final int numCols;

	// dimensions
	private final int tileSize = 60;
	private final int width, height;

	// position
	private float x;
	private float y;
	private final float z = 50;
	private float xmin, ymin;
	private float followStrength;

	// rendering
	private TextureAtlas tileset;
	private final TileElement[][] screenTiles;
	private final int rowsToDraw;
	private final int colsToDraw;
	private int rowOffset, colOffset;
	private final int tilePad = 2;

	public TileMap(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		width = numCols * tileSize;
		height = numRows * tileSize;
		rowsToDraw = Config.DEFAULT_RESOLUTION_HEIGHT / tileSize + tilePad;
		colsToDraw = Config.DEFAULT_RESOLUTION_WIDTH / tileSize + tilePad;
		screenTiles = new TileElement[rowsToDraw][colsToDraw];
		Logger.log("Tile Size = " + tileSize + "; Using padding = " + tilePad + ", to fit the screen the TileMap " +
				"will draw " + rowsToDraw + " rows and " + colsToDraw + " cols.");
	}

	// INITIALIZATION

	public void initTileElements() {
		for (int screenRow = 0; screenRow < rowsToDraw; ++screenRow) {
			for (int screenCol = 0; screenCol < colsToDraw; ++screenCol) {
				float xPos = x + screenCol * tileSize;
				float yPos = y + screenRow * tileSize;
				screenTiles[screenRow][screenCol] = new TileElement(tileset, map[screenRow][screenCol], xPos, yPos, z, tileSize);
			}
		}
	}

	// GETTERS

	public byte[][] getMap() { return map; }
	public int getTileSize() { return tileSize; }
	public int getNumRows() {
		return numRows;
	}
	public int getNumCols() {
		return numCols;
	}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public float getX() { return x; }
	public float getY() { return y; }
	public float getZ() { return z; }
	public float getFollowStrength() { return followStrength; }
	public TextureAtlas getTileset() { return tileset; }

	public byte getValue(int row, int col) {
		return map[row][col];
	}

	public boolean isBlocked(int row, int col) {
		try {
			return blocked[row][col];
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	// SETTERS

	public void setMap(byte[][] map) {
		this.map = map;
	}
	public void setBlocked(boolean[][] blocked) { this.blocked = blocked; }
	public void setFollowStrength(float followStrength) { this.followStrength = followStrength; }
	public void setTileset(TextureAtlas tileset) { this.tileset = tileset; }

	public void setValue(int row, int col, byte value) {
		if (row < 0 || col < 0 || row >= numRows || col >= numCols)
			Logger.logError("Tried to modify tile out of bounds (" + row + "," + col + ")");
		else {
			map[row][col] = value;
		}
	}

	public void setBlocked(int row, int col, boolean isBlocked) {
		blocked[row][col] = isBlocked;
	}

	// move tilemap
	public void setPosition(Vector2f positionVector) {
		setPosition(positionVector.x(), positionVector.y());
	}

	public void setPosition(float x, float y) {
		float timeRatio = 60.0f / Display.FPS_CAP;
		if (followStrength != 0) {
			this.x += (timeRatio * followStrength * (x - this.x));
			this.y += (timeRatio * followStrength * (y - this.y));
		}
		else {
			this.x = x;
			this.y = y;
		}
		rowOffset = (int) (-this.y / tileSize);
		colOffset = (int) (-this.x / tileSize);
	}

	// set position instantly
	public void jumpPosition(Vector2f position) {
		jumpPosition(position.x, position.y);
	}

	public void jumpPosition(float x, float y) {
		this.x = x;
		this.y = y;
		rowOffset = (int) (-this.y / tileSize);
		colOffset = (int) (-this.x / tileSize);
	}

	// UPDATING

	// RENDERING
	public void render() {
		// update tiles list, sending each to the MasterRenderer to be rendered
		for (int row = rowOffset - tilePad / 2, screenRow = 0; row < rowOffset + rowsToDraw; ++row, ++screenRow) {
			for (int col = colOffset - tilePad / 2, screenCol = 0; col < colOffset + colsToDraw; ++col, ++screenCol) {
				// ignore out of bounds tiles (can implement a special case here later)
				if (row >= numRows || row < 0 || col >= numCols || col < 0) continue;
				// ignore zero tiles (always empty)
				if (map[row][col] == 0) continue;
				// ignore breaking the screen array bounds
				if (screenRow >= rowsToDraw || screenCol >= colsToDraw) continue;
				// otherwise, update and add to draw call
				float xPos = x + col * tileSize;
				float yPos = y + row * tileSize;
				screenTiles[screenRow][screenCol].setTextureIndex(map[row][col]);
				screenTiles[screenRow][screenCol].setPosition(xPos, yPos);
				Render.drawTile(screenTiles[screenRow][screenCol]);
			}
		}
	}

}