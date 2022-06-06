#version 140

in vec2 textureCoords;
in vec4 pass_color;

out vec4 out_color;

in vec2 pos;
in float blend;

uniform float numRows;

void main() {

//    vec2 scaledPos = pos * 2;

    float alpha = 1 - dot(pos, pos);
    alpha -= blend;
    alpha = pow(alpha, 16);
    out_color = vec4(pass_color.xyz, alpha);

    if (numRows == 1000) out_color += vec4(1);

}
