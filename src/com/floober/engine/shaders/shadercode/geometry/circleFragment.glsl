#version 150

out vec4 out_color;

uniform vec4 color;
uniform vec2 center;
uniform float innerRadius;
uniform float outerRadius;
uniform vec2 portion;

varying vec2 pos;

void main(void) {

	float offset = -portion.y + 270;

	float angle = degrees( atan( pos.y, pos.x ) ) + 180; // add 180 to get range [0 ... 360], then adjust with offset

	float start = offset;
	float end = mod(portion.x * 360 + offset, 360);

	if (start > end) {
		if (!(angle > start || angle < end)) discard;
	}
	else {
		if (!(angle > start && angle < end)) discard;
	}

	float dist = sqrt(dot(pos, pos));

	// discard fragments outside outer radius or inside inner radius
	if (dist < innerRadius)
		discard;
	else if (dist > outerRadius)
		discard;
	else
		out_color = color;

}