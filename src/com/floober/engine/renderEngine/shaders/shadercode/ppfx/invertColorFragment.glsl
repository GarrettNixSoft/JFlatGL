#version 140

in vec2 textureCoords;
out vec4 out_Color;
uniform sampler2D colorTexture;

void main() {

	out_Color = 1 - texture(colorTexture, textureCoords);

}
