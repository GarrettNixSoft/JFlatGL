#version 140

out vec4 out_color;

uniform vec4 color;
uniform float r;
uniform int roundMode;
uniform vec2 dimensions;

in vec2 pos;

float udRoundBox(vec2 pos, vec2 ext) {
	return length(max(abs(pos) + vec2(r) - ext, 0.0)) - r;
}

void main(void) {

	vec2 aspectRatio = roundMode == 0 ? vec2(1, dimensions.x / dimensions.y) : vec2(dimensions.y / dimensions.x, 1);

	vec2 pos2 = pos * aspectRatio;
	vec2 halfRes = vec2(0.5) * aspectRatio;

	// compute box
	float b = udRoundBox(pos2 - halfRes, halfRes);

	// colorize
//	out_color = mix(color, vec4(0), smoothstep(0,1,b));
	out_color = color * step(b, 0.0);


//	out_color = color;
//
//	float h = r * ratio;
//
//	vec2 tl = vec2(h,r);
//	vec2 tr = vec2(1-h,r);
//	vec2 bl = vec2(h,1-r);
//	vec2 br = vec2(1-h,1-r);
//
//	if (pos.x < tl.x && pos.y < tl.y) { // top left
//		out_color = vec4(1,0,0,1);
//		if (distance(pos, tl) > r) discard;
//	}
//	else if (pos.x > br.x && pos.y > br.y) { // bottom right
//		out_color = vec4(0,0,1,1);
//	}
//	else if (pos.x > tr.x && pos.y < tr.y) { // top right
//		out_color = vec4(1,1,0,1);
//	}
//	else if (pos.x < bl.x && pos.y > br.y) { // bottom left
//		out_color = vec4(0,1,0,1);
//	}

//	out_color *= length(diff);

}