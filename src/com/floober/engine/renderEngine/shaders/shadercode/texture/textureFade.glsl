#version 140

// fade settings
uniform vec2 minmax;
uniform vec2 offsets;
uniform int direction; // 0 = left-to-right; 1 = right-to-left; 2 = top-to-bottom; 3 = bottom-to-top

varying vec2 fragPos;

void fade(inout vec4 color) {

	// explode uniform vectors
	float min = minmax.x;
	float max = minmax.y;
	float startOffset = offsets.x;
	float endOffset = offsets.y;

	vec2 position = fragPos + vec2(0.5);

	float value;
	switch (direction) {
		case 0:
			value = 1.0 - position.x;
			break;
		case 1:
			value = position.x;
			break;
		case 2:
			value = position.y;
			break;
		case 3:
			value = 1.0 - position.y;
			break;
		default:
			value = 0;
	}

	float a;
	if (value < startOffset) a = min;
	else if (value > endOffset) a = max;
	else a = smoothstep(startOffset, endOffset, value); // smoothstep
//	else a = (value - startOffset) / (endOffset - startOffset) + min; // linear

	color.a *= a;

	// debug
//	color.a *= value;

}
