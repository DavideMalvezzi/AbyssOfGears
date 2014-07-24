#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

#define MAX_RADIUS 250

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform int samples_count;
uniform float weight[MAX_RADIUS];
uniform vec2 pixelSize;


void main(){

	vec4 color = texture2D(u_texture, v_texCoords) * weight[0];
	
	for(int i=1; i<samples_count/2; i++){
		color+= texture2D(u_texture,  v_texCoords + pixelSize*i) * weight[i];
		color+= texture2D(u_texture,  v_texCoords - pixelSize*i) * weight[i];
	}
	
	
	gl_FragColor =  color;
	
	
}

