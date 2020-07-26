#version 140

in vec3 position;

uniform vec4 color;
uniform mat4 transformationMatrix;

void main(void) {

	gl_Position = transformationMatrix * vec4(position, 1.0);

}