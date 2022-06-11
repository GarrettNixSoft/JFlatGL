#version 330

in vec2 pass_textureCoords;
out vec4 out_color;

uniform vec4 color;
uniform sampler2D fontAtlas;

uniform float width;
uniform float edge;
uniform float borderWidth;
uniform float borderEdge;

uniform vec2 shadowOffset;
uniform vec4 outlineColor;

void main(void){

	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	float borderDistance = 1.0 - texture(fontAtlas, pass_textureCoords - shadowOffset).a;
	float borderAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, borderDistance);

	float finalAlpha = alpha + (1.0 - alpha) * borderAlpha;
	vec4 finalColor = mix(outlineColor, color, alpha / finalAlpha);

	out_color = vec4(finalColor.xyz, finalAlpha * color.w);

//	out_color = vec4(color.w * distance, color.w * distance, color.w * distance, min(1, 1 + width + edge + borderWidth + borderEdge + shadowOffset.x + outlineColor.x));

}