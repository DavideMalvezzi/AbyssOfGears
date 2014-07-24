#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
uniform vec2 position;
uniform vec2 dimension;

void main(){
	vec2 pos = (gl_FragCoord.xy)/dimension+0.5;
	
	gl_FragColor = vec4(pos.y) ;
	
	
}
