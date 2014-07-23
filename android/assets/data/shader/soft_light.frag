#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform LOWP vec4 mixColor;

void main(){

	vec4 texture_color = texture2D(u_texture, v_texCoords)*v_color;
	
	//gl_FragColor =  (1.0-2.0*mixColor)*texture_color*texture_color + 2.0*mixColor*texture_color;
	gl_FragColor = 2.0*texture_color*(1.0-mixColor) + sqrt(texture_color)*(2.0*mixColor-1.0);
	
}

