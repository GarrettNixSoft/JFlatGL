#version 140

in vec2 position;
in mat4 transformationMatrix;
in vec4 textureCoordOffsets;
in float alpha;

out vec2 textureCoords;
flat out float pass_alpha;

void main() {

	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
	textureCoords *= textureCoordOffsets.zw; // scale down size [0 -> 1] to [0 -> size]
	textureCoords += textureCoordOffsets.xy; // translate to starting offset

	pass_alpha = alpha;

	gl_Position = transformationMatrix * vec4(position, 1.0, 1.0);

}