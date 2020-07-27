#version 140

in vec3 position;

out vec2 textureCoords;

uniform mat4 transformationMatrix;
uniform vec4 textureOffset;

void main(void) {

	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
	textureCoords *= textureOffset.zw; // scale down size [0 -> 1] to [0 -> size]
	textureCoords += textureOffset.xy; // translate to starting offset

	gl_Position = transformationMatrix * vec4(position, 1.0);

}