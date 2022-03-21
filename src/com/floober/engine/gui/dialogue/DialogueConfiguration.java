package com.floober.engine.gui.dialogue;

import com.floober.engine.core.renderEngine.fonts.fontMeshCreator.FontType;
import org.joml.Vector4f;

public record DialogueConfiguration(FontType defaultTextFont, FontType defaultNameFont, Vector4f defaultTextColor,
									Vector4f defaultNameColor, Vector4f defaultBackgroundColor,
									float defaultTextSize, float defaultNameSize) {}