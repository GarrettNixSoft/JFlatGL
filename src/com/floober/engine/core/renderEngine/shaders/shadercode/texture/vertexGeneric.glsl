#version 140

in vec2 position;
out vec2 textureCoords;

varying vec2 pos;

void main() {

    pos = position.xy;
    gl_Position = vec4(position, 0.0, 1.0);
    textureCoords = position * 0.5 + 0.5;

}