#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform LOWP vec3 mixColor;

void main(){

	vec4 texture_color = texture2D(u_texture, v_texCoords)*v_color;
	
	gl_FragColor =  vec4(texture_color.rgb / (1.0-mixColor.rgb), texture_color.a);
	
	
}

