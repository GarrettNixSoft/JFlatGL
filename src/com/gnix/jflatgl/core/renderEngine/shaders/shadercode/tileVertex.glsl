#version 140

// IN VARIABLES
in vec3 position;
in mat4 transformationMatrix;
in vec4 texOffsets;

out vec2 textureCoords;

in mat4 overlayTransformationMatrix;

in vec2 in_modifiers;
in vec4 in_rChannelColor;
in vec4 in_gChannelColor;
in vec4 in_bChannelColor;
in vec4 in_aChannelColor;

in float in_overlayAlpha;

out float doColorSwap;
out vec4 rChannelColor;
out vec4 gChannelColor;
out vec4 bChannelColor;
out vec4 aChannelColor;

out float doOverlay;
out vec2 overlayTextureCoords;
out float overlayAlpha;

// UNIFORMS

void main(void) {

	// get relative texture coords in this tile from 0 to 1
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);

	// get the size of a tile in the tileset atlas
	float tileSize = texOffsets.z - texOffsets.x;
	float size = tileSize / 1.0;
	textureCoords *= size;

	// use smoothstep to find the corresponding texture sub-coords inside this tile's region of the tileset
    textureCoords = textureCoords + texOffsets.xy;

	overlayTextureCoords = (vec4(position, 1.0) * overlayTransformationMatrix).xy;

	// PASS VARIABLES
	doColorSwap = in_modifiers.x;
	rChannelColor = in_rChannelColor;
	gChannelColor = in_gChannelColor;
	bChannelColor = in_bChannelColor;
	aChannelColor = in_aChannelColor;

	doOverlay = in_modifiers.y;
	overlayAlpha = in_overlayAlpha;

	// set the position of this vertex in the world
    gl_Position = transformationMatrix * vec4(position, 1.0);

}