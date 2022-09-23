#version 150

out vec4 out_color;

uniform vec4 color;
uniform float innerRadius;
uniform float outerRadius;
uniform vec2 portion;
uniform float smoothness;

varying vec2 pos;

// function declaration! yay!
void radiusTest(float);

void main(void) {

	float dist = sqrt(dot(pos, pos));

	// if it's 100% don't even bother checking the angles
	if (portion.x >= 1) {
		radiusTest(dist);
		return;
	}

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

	// discard fragments outside outer radius or inside inner radius;
	// ones that are close to the edges should be smoothed according the
	// current smoothness value
	radiusTest(dist);

}

void radiusTest(float dist) {
	if (dist < innerRadius) {
		if (dist > innerRadius - smoothness) {
			float a = smoothstep(innerRadius - smoothness, innerRadius, dist);
			out_color = color;
			out_color.a *= a;
		}
		else discard;
	}

	else if (dist > outerRadius) {
		if (dist < outerRadius + smoothness) {
			float a = 1.0 - smoothstep(outerRadius, outerRadius + smoothness, dist);
			out_color = color;
			out_color.a *= a;
		}
		else discard;
	}
	else
		out_color = color;
}