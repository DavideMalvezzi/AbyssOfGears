#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform vec2 pixel;

void main(){

	vec4 color = vec4(0.0);
	
	for(float i=-9.0; i<=9.0; i+=1.0){
		for(float j=-9.0; j<=9.0; j+=1.0){
			color+=texture2D(u_texture, v_texCoords + pixel*vec2(i,j));
		}
	}
	
	color/=324.0;
			
	gl_FragColor = v_color * color ;
	
	
}

