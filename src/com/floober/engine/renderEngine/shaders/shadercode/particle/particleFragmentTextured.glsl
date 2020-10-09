#version 140

in vec2 textureCoords;
flat in float pass_alpha;

out vec4 color;

uniform sampler2D textureSampler;

void main() {

	color = texture(textureSampler, textureCoords);
	color.a *= pass_alpha;

}