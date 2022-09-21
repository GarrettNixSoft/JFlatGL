#version 140

in vec3 position;

uniform vec4 textureOffset;

out vec2 textureCoords;

void main() {

	gl_Position = vec4(position, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, (position.y+1.0)/2.0);
	textureCoords *= textureOffset.zw; // scale down size [0 -> 1] to [0 -> size]
	textureCoords += textureOffset.xy; // translate to starting offset

}
