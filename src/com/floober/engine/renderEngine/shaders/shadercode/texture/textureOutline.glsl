#version 140

// parameters
in vec2 textureCoords;
out vec4 out_color;

// the texture to sample
uniform sampler2D textureSampler;

// step size
uniform vec2 stepSize;

// color
uniform vec4 outlineColor;

void main(void) {

	// get the alpha at this texel
	float alpha = texture(textureSampler, textureCoords).a;

	// if it's at the border and alpha == 1, pre-rig for outline to work
	if ((textureCoords.x + stepSize.x > 1 || textureCoords.y + stepSize.y > 1 ||
		textureCoords.x - stepSize.x < 0 || textureCoords.y - stepSize.y < 0)
		&& alpha == 1)
		alpha += .25;

	alpha *= 4;

	// do the outline convolution
	alpha -= texture(textureSampler, textureCoords + vec2(stepSize.x, 0)).a;
	alpha -= texture(textureSampler, textureCoords + vec2(-stepSize.x, 0)).a;
	alpha -= texture(textureSampler, textureCoords + vec2(0, stepSize.y)).a;
	alpha -= texture(textureSampler, textureCoords + vec2(0, -stepSize.y)).a;
//	alpha -= texture(textureSampler, textureCoords + vec2(stepSize.x, stepSize.y)).a;
//	alpha -= texture(textureSampler, textureCoords + vec2(-stepSize.x, stepSize.y)).a;
//	alpha -= texture(textureSampler, textureCoords + vec2(stepSize.x, -stepSize.y)).a;
//	alpha -= texture(textureSampler, textureCoords + vec2(-stepSize.x, -stepSize.y)).a;

	out_color = outlineColor;
	out_color.a *= alpha;

}