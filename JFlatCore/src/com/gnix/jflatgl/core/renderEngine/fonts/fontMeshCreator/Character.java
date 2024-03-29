package com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator;

/**
 * Simple data structure class holding information about a certain glyph in the
 * font texture atlas. All sizes are for a font-size of 1.
 * 
 * @author Karl
 *
 * @param id
 *            - the ASCII value of the character.
 * @param xTextureCoord
 *            - the x texture coordinate for the top left corner of the
 *            character in the texture atlas.
 * @param yTextureCoord
 *            - the y texture coordinate for the top left corner of the
 *            character in the texture atlas.
 * @param xTexSize
 *            - the width of the character in the texture atlas.
 * @param yTexSize
 *            - the height of the character in the texture atlas.
 * @param xOffset
 *            - the x distance from the cursor to the left edge of the
 *            character's quad.
 * @param yOffset
 *            - the y distance from the cursor to the top edge of the
 *            character's quad.
 * @param sizeX
 *            - the width of the character's quad in screen space.
 * @param sizeY
 *            - the height of the character's quad in screen space.
 * @param xAdvance
 *            - how far in pixels the cursor should advance after adding
 *            this character.
 *
 * NOTE: I've also turned this class into a Record.
 * - Floober
 */
public record Character(int id, char c, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize,
					   double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {

	public double xMaxTextureCoord() {
		return xTexSize + xTextureCoord;
	}

	public double yMaxTextureCoord() {
		return yTexSize + yTextureCoord;
	}

}



