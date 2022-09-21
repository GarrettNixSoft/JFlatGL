#version 140

in vec2 textureCoords;
out vec4 out_color;
uniform sampler2D colorTexture;

void main() {

	vec4 color = texture(colorTexture, textureCoords);
	float av = (color.r + color.g + color.b) / 3;
	out_color = vec4(av, av, av, color.a);

}
