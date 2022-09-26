#version 140

out vec4 out_color;

uniform vec2 lightPosition;
uniform float ambientLight;
uniform float lightIntensity;
uniform float lightRadius;
uniform float lightInnerRadius;
uniform vec3 lightColor;

in vec2 pos;

void main() {

	// get distance from light center
	vec2 displacement = pos - lightPosition;
	displacement.y /= (16.0 /9.0); // correct for aspect ratio
	float dist = sqrt(dot(displacement, displacement));

	// calculate light based on that
	float addedLight = 0;
	if (dist < lightInnerRadius) addedLight = lightIntensity;
	else if (dist < lightRadius) addedLight = lightIntensity * (1.0 - smoothstep(lightInnerRadius, lightRadius, dist));

	// add upp the final light value, and subtract it from 1 to get the amount of shading to apply to this fragment
	float lightTotal = ambientLight + addedLight;
	float finalLight = 1.0 - lightTotal;

	// get the resulting color
	out_color = vec4(lightColor, finalLight);

}