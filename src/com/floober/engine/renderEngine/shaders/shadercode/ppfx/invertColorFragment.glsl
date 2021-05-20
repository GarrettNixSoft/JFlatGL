#version 140

in vec2 textureCoords;
out vec4 out_Colour;
uniform sampler2D colourTexture;

void main() {

	out_Colour = 1 - texture(colourTexture, textureCoords);

}
