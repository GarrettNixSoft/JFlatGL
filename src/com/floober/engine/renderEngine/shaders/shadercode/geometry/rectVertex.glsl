#version 140

in vec3 position;

uniform mat4 transformationMatrix;

out vec2 pos;

void main(void) {

//	pos = position.xy;
	pos = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);

	gl_Position = transformationMatrix * vec4(position, 1.0);

}