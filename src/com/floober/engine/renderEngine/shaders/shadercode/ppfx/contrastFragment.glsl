#version 140

in vec2 textureCoords;
out vec4 out_Color;

uniform float contrast;
uniform sampler2D colourTexture;

void main() {

	out_Color = texture(colourTexture, textureCoords);
	out_Color.rgb = (out_Color.rgb - 0.5) * (1.0 + contrast) + 0.5;

}
