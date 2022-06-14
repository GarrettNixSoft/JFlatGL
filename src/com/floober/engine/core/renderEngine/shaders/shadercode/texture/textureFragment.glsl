#version 150

// parameters
in vec2 textureCoords;
layout(origin_upper_left) in vec4 gl_FragCoord;

// output
out vec4 out_color;

// the texture to sample
uniform sampler2D textureSampler;
uniform float textureAlpha;

// turn color effects on or off
uniform bool doColor;
uniform vec4 color;
uniform float mixVal;

// turn on or off color swap
uniform bool doColorSwap;

// turn lighting calculations on or off
uniform bool doLighting;

// turn glitch effects on or off
uniform bool doGlitch;

// turn outline on or off
uniform bool doOutline;

// turn fading on or off
uniform bool doFade;

// other shader functions
void colorSwap(inout vec4, vec4);
void light(inout vec4);
void glitch(inout vec4);
void fade(inout vec4);

void main(void){

	vec4 textureColor = texture(textureSampler, textureCoords);
	out_color = textureColor;
	out_color.a *= textureAlpha;

	if (doColorSwap) {
		colorSwap(out_color, textureColor);
	}

	if (doColor) {
		float a = out_color.a;
		out_color = mix(out_color, color, mixVal);
		out_color.a = a;
	}

	if (doLighting) {
		light(out_color);
	}

	if (doGlitch) {
		float a = out_color.a;
		glitch(out_color);
		out_color.a = a;
	}

	if (doFade) {
		fade(out_color);
	}

}