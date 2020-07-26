#version 140

in vec3 position;

uniform mat4 transformationMatrix;

varying vec2 pos;

void main(void) {

	pos = position.xy;
	gl_Position = transformationMatrix * vec4(position, 1.0);

}