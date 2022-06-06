package com.floober.engine.core.renderEngine.elements.tile;

import com.floober.engine.core.renderEngine.textures.TextureComponent;
import org.joml.Vector4f;

public record TileSettings(int atlasIndex, float rotation, boolean hasTransparency, boolean doColorSwap,
						   Vector4f rChannelColor, Vector4f gChannelColor, Vector4f bChannelColor, Vector4f aChannelColor,
						   boolean doOverlay, TextureComponent overlayTexture, float overlayAlpha, float overlayRotation) {}
