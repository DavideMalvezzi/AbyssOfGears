#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform int isVertical;
uniform float gloomFactor;
uniform vec2 pixelSize;

void main(){

	vec2 pos = v_texCoords;
	
	float values[9];
	values[0]=0.05;
	values[1]=0.09;
	values[2]=0.11;
	values[3]=0.15;
	values[4]=0.2;
	values[5]=0.15;
	values[6]=0.11;
	values[7]=0.09;
	values[8]=0.05;
	
	
	vec4 result = vec4(0);
	vec4 color = vec4(0);
	vec4 bg_color = vec4(0.0,0.0,0.0,1.0);
	vec4 sum_color = vec4(0);
	
	
	if(isVertical == 1){
		vec2 curSamplePos=vec2(pos.x,pos.y-4.0*pixelSize.y);
		for(int i=0;i<9;i++){
			color = texture2D(u_texture,curSamplePos);
			
			sum_color.rgb = mix(bg_color.rgb, color.rgb, color.a);
			sum_color.a = mix(color.a, bg_color.a, color.a);
			
			result.rgba+=  sum_color * values[i];
			
			curSamplePos.y+=pixelSize.y;
		}
		
	}else if(isVertical == -1){
	
		vec2 curSamplePos=vec2(pos.x-4.0*pixelSize.x,pos.y);
		for(int i=0;i<9;i++){
			color = texture2D(u_texture,curSamplePos);
			
			sum_color.rgb = mix(bg_color.rgb, color.rgb, color.a);
			sum_color.a = mix(color.a, bg_color.a, color.a);
			
			result.rgba+=  sum_color.rgba * values[i];
			
			curSamplePos.x+=pixelSize.x;
		}
	}
	
	gl_FragColor = v_color * vec4(1.0-result.rgb, result.a*gloomFactor) ;
	
	
}

