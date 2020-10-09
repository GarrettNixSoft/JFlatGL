#version 140

// in variables
flat in vec4 pass_color;
flat in float pass_innerRadius;
flat in float pass_outerRadius;
flat in float pass_lightMode;
flat in vec3 pass_lightColor;
flat in float pass_lightIntensity;

// position in quad
varying vec2 pos;

// resulting fragment value
out vec4 out_color;

void main() {

	// distance from center
	float radius = sqrt(dot(pos, pos)) * 2;

	// particle color
	vec4 particleColor;
	// if inside min radius, fade from pure white to particle color
	if (radius < pass_innerRadius) {
		particleColor = mix(vec4(1, 1, 1, pass_color.a), pass_color, radius / pass_innerRadius);

	}
	// otherwise, fade from full color to nothing
	else if (radius < pass_outerRadius) {
		particleColor = pass_color;
		particleColor.a *= 1 - smoothstep(pass_innerRadius, pass_outerRadius, radius);
	}
	else particleColor = vec4(0);

	// light color
	float alpha;
	// linear mode
	if (bool(pass_lightMode)) {
		alpha = 1 - radius;
	}
	// smooth mode
	else {
		alpha = 1 - smoothstep(0, 1, radius);
	}
	vec4 lightColor = vec4(pass_lightColor, alpha * pass_lightIntensity * pass_color.a);

	// final color
	out_color = lightColor + particleColor;
}