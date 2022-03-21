#version 120

in vec3 position;
out vec2 textureCoords;

uniform mat4 transformationMatrix;

void main() {

	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
	gl_Position = transformationMatrix * vec4(position, 1.0);

}