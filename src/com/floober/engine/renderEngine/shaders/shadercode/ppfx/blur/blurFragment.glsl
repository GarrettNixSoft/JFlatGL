#version 430

out vec4 out_color;

in vec2 blurTexCoords[11];

uniform sampler2D originalTexture;

const float weights[] = {0.0093,0.028002,0.065984,0.121703,0.175713,0.198596,0.175713,0.121703,0.065984,0.028002,0.0093};

void main() {

	out_color = vec4(0.0);

	for (int i = 0; i < 11; i++) {
		out_color += texture(originalTexture, blurTexCoords[i]) * weights[i];
	}

	// brighten it by 50%
//	out_color.a *= 1.5;

}
