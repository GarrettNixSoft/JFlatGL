#version 140

// in variables used up here
in vec2 position;
in mat4 transformationMatrix;

// in variables that need to be passed
in float innerRadius;
in float outerRadius;
in vec4 color;
in int lightMode;
in vec3 lightColor;
in float lightIntensity;

flat out vec4 pass_color;
flat out float pass_innerRadius;
flat out float pass_outerRadius;
flat out float pass_lightMode;
flat out vec3 pass_lightColor;
flat out float pass_lightIntensity;

// the position in the quad
varying vec2 pos;

void main() {

	// set positions
	pos = position.xy;
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);

	// pass values
	pass_color = color;
	pass_innerRadius = innerRadius;
	pass_outerRadius = outerRadius;
	pass_lightMode = lightMode;
	pass_lightColor = vec3(lightColor);
	pass_lightIntensity = lightIntensity;

}