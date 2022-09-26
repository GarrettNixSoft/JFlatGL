#version 330

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform vec3 translation;

void main(void){

	vec3 translationFixed = translation * vec3(2.0, -2.0, 0);

	gl_Position = vec4(position + translationFixed, 1.0);
	pass_textureCoords = textureCoords;

}