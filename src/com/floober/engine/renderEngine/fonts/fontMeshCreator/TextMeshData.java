package com.floober.engine.renderEngine.fonts.fontMeshCreator;

/**
 * Stores the vertex data for all the quads on which a text will be rendered.
 * @author Karl
 *
 * NOTE: Now that Java 15 is around, I've turned this class into a Record. It
 * functions exactly the same except it's all one line. Neat!
 * - Floober
 */
public record TextMeshData(float[] vertexPositions, float[] textureCoords, float textHeight) {}