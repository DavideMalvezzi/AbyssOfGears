#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float BloomThreshold;

void main(){

	vec4 color = texture2D(u_texture, v_texCoords);
	
	gl_FragColor = v_color * clamp((color - BloomThreshold) / (1.0 - BloomThreshold), vec4(0.0), vec4(1.0)) ;
	
	
}

    