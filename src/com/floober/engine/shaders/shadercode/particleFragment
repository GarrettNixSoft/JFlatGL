#version 140

in vec2 textureCoords;
in vec4 pass_color;

out vec4 out_color;

in vec2 textureCoords1;
in vec2 textureCoords2;
in float blend;

uniform sampler2D particleTexture;

void main(void) {

	// if rect: out_color = color;

	vec4 color1 = texture(particleTexture, textureCoords1);
	vec4 color2 = texture(particleTexture, textureCoords2);
	vec4 mixedColor = mix(color1, color2, blend);

	float alpha = mixedColor.a * pass_color.a;
	out_color = vec4(pass_color.xyz, alpha);

}