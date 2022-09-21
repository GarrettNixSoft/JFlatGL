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

void main() {

	float alpha = 0;

//	if (texture(textureSampler, textureCoords).a == 1) {
//		alpha = 1;
//	}
//	else if (texture(textureSampler, textureCoords + vec2(stepSize.x, 0)).a == 1) {
//		alpha = 1;
//	}
//	else if (texture(textureSampler, textureCoords + vec2(-stepSize.x, 0)).a == 1) {
//		alpha = 1;
//	}
//	else if (texture(textureSampler, textureCoords + vec2(0, stepSize.y)).a == 1) {
//		alpha = 1;
//	}
//	else if (texture(textureSampler, textureCoords + vec2(0, -stepSize.y)).a == 1) {
//		alpha = 1;
//	}

	alpha += texture(textureSampler, textureCoords).a;
	alpha += texture(textureSampler, textureCoords + vec2(stepSize.x, 0)).a;
	alpha += texture(textureSampler, textureCoords + vec2(-stepSize.x, 0)).a;
	alpha += texture(textureSampler, textureCoords + vec2(0, stepSize.y)).a;
	alpha += texture(textureSampler, textureCoords + vec2(0, -stepSize.y)).a;
//	alpha += texture(textureSampler, textureCoords + vec2(stepSize.x, stepSize.y)).a;
//	alpha += texture(textureSampler, textureCoords + vec2(-stepSize.x, stepSize.y)).a;
//	alpha += texture(textureSampler, textureCoords + vec2(stepSize.x, -stepSize.y)).a;
//	alpha += texture(textureSampler, textureCoords + vec2(-stepSize.x, -stepSize.y)).a;

	out_color = outlineColor;
	out_color.a *= alpha;

}