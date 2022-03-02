#version 140

out vec4 out_color;

uniform vec4 color;
uniform float r;
uniform vec2 dimensions;
uniform float lineWidth;

in vec2 pos;

float udRoundBox(vec2 pos, vec2 ext) {
	return length(max(abs(pos) + vec2(r) - ext, 0.0)) - r;
}

void main(void) {

	// STEP 1: Discard if inside lineWidth
	if (pos.x < lineWidth || pos.x > 1 - lineWidth || pos.y < lineWidth || pos.y > 1 - lineWidth) {
		out_color = vec4(lineWidth);

		// STEP 2: If not discarded, draw like normal rect
		vec2 aspectRatio = vec2(1, dimensions.x / dimensions.y);

		vec2 pos2 = pos * aspectRatio;
		vec2 halfRes = vec2(0.5) * aspectRatio;

		// compute box
		float b = udRoundBox(pos2 - halfRes, halfRes);

		// colorize
		//	out_color = mix(color, vec4(0), smoothstep(0,1,b));
		out_color = color * step(b, 0.0);
	}
	else discard;



}
