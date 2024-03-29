#version 140

in vec2 position;

in mat4 transformationMatrix;
in vec4 texOffsets;
in float blendFactor;
in vec4 color;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;
out vec4 pass_color;

uniform float numRows;

out vec2 pos;

void main(void) {

	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords /= numRows;
	textureCoords1 = textureCoords + texOffsets.xy;
	textureCoords2 = textureCoords + texOffsets.zw;
	blend = blendFactor;

	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	pass_color = color;
	pos = position;

}