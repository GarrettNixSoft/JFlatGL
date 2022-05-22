package gametitle.util;

import com.floober.engine.core.util.color.ColorConverter;
import org.joml.Vector4f;

public class Definitions {

	public static final Vector4f CONSOLE_GREEN = new Vector4f(0, 0.8f, 0, 1);
	public static final Vector4f CONSOLE_BOX = new Vector4f(0.5f, 0.5f, 0.5f, 0.1f);

	public static Vector4f getColorByName(String colorName) {
		return switch (colorName) {
			case "console_green" -> CONSOLE_GREEN;
			case "console_box" -> CONSOLE_BOX;
			default -> ColorConverter.getColorByName(colorName);
		};
	}

}
