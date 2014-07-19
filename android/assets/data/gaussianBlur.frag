#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

#define MAX_RADIUS  25

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int samples_count;
uniform float weight[MAX_RADIUS];
uniform vec2 offset[MAX_RADIUS];


void main(){

	vec4 color = vec4(0.0);
	
	for(int i=0; i<samples_count; i++){
		color+= texture2D(u_texture, v_texCoords + offset[i]) * weight[i];
	}
	
	
	gl_FragColor = v_color * color;
	
	
}

