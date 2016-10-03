#version 330 core

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec3 in_A;
layout(location = 2) in vec3 in_B;
layout(location = 3) in vec3 in_C;
layout(location = 4) in vec3 in_D;
layout(location = 5) in vec3 in_color;

uniform float condline_shown_Colour_r;
uniform float condline_shown_Colour_g;
uniform float condline_shown_Colour_b;


uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform float zoom;

uniform float showAll;
uniform float condlineMode;

out vec4 sceneColor;

void main()
{
	mat4 pvm = projection * view * model;
	gl_Position = pvm * vec4(in_position, 1.0f);	
	vec4 A = pvm * vec4(in_A, 1.0f);
	vec4 B = pvm * vec4(in_B, 1.0f);
	vec4 C = pvm * vec4(in_C, 1.0f);
	vec4 D = pvm * vec4(in_D, 1.0f);	
	
	vec4 N = vec4(A.y - B.y, B.x - A.x, 0.0f, 1.0f);
	
	if (condlineMode < 1.0f) {
		if (showAll > 0.0f || zoom / dot(N, C - A) * dot(N, D - A) > -1.0e-20f) {
			sceneColor = vec4(in_color.r, in_color.g, in_color.b, 1.0f);
		} else {
			sceneColor = vec4(0.0f, 0.0f, 0.0f, 0.0f);
		}
	} else {
		if (zoom / dot(N, C - A) * dot(N, D - A) > -1.0e-20f) {
			sceneColor = vec4(condline_shown_Colour_r, condline_shown_Colour_g, condline_shown_Colour_b, 1.0f);
		} else {
			sceneColor = vec4(in_color.r, in_color.g, in_color.b, 1.0f);
		}
	}
}