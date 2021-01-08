#version 140

in vec2 textureCoords;

// colors per channel (default 0 to disable)
uniform vec4 rChannelColor = vec4(0);
uniform vec4 gChannelColor = vec4(0);
uniform vec4 bChannelColor = vec4(0);
uniform vec4 aChannelColor = vec4(0);

void colorSwap(inout vec4 color, vec4 texColor) {

	vec4 rChannel = rChannelColor * texColor.r;
	vec4 gChannel = gChannelColor * texColor.g;
	vec4 bChannel = bChannelColor * texColor.b;
	vec4 aChannel = aChannelColor * texColor.a;

	color = rChannel + gChannel + bChannel + aChannel;

}