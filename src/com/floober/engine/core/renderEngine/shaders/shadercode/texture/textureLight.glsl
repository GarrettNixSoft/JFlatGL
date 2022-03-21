#version 150

// parameters
in vec2 textureCoords;
layout(origin_upper_left) in vec4 gl_FragCoord;

// the texture to sample
uniform sampler2D textureSampler;

// lighting constants
const int max_lights = 8;
const float a = 0.5;
const float b = 0.0;

// adjust coordinates based on screen size
uniform vec2 screenRatio;

// light values
uniform float ambientLight;
uniform float textureAlpha;
uniform vec2 lightPositions[max_lights];
uniform vec4 lightColors[max_lights];
uniform float lightIntensities[max_lights];
uniform float lightInnerRadii[max_lights];
uniform float lightOuterRadii[max_lights];
uniform float lightMaxRadii[max_lights];

void light(inout vec4 pass_color) {

    // fix screen coordinates
    vec2 frag_position = gl_FragCoord.xy * screenRatio;

    // light calculations
    vec4 totalLight = vec4(0.0);

    for (int i = 0; i < max_lights; i++) {
        if (lightOuterRadii[i] == -1) continue; // skip empty lights
        vec2 lightVector = frag_position - lightPositions[i];
        float distance = length(lightVector);
        float radius = lightOuterRadii[i] - lightInnerRadii[i];
        if (distance <= lightInnerRadii[i]) {
            float att = radius / (radius + a * distance + b * distance * distance);
            att = max(att, 1.0);
            totalLight += lightColors[i] * lightIntensities[i] * att;
        }
        else {
            if (distance < lightMaxRadii[i]) {
                distance -= lightInnerRadii[i];
                float att = radius / (radius + a * distance + b * distance * distance);
                if (att < 0.02) att = 0; // lower bound 0.02; anything lower gets ignored
                totalLight += lightColors[i] * lightIntensities[i] * att;
            }
        }
    }

    totalLight = max(totalLight, ambientLight);
    vec4 textureColor = texture(textureSampler, textureCoords);
    textureColor.w *= textureAlpha;
    pass_color = textureColor * totalLight;

}