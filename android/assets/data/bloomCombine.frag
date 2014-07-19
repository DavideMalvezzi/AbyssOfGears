#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;


uniform sampler2D u_texture;
uniform sampler2D u_base;

uniform float BloomIntensity;
uniform float BaseIntensity;
 
uniform float BloomSaturation;
uniform float BaseSaturation;

vec4 adjustSaturation(vec4 color, float sat){
 	float g = dot(color.rgb, vec3(0.3, 0.59, 0.11));
 	
 	vec4 grey = vec4(g,g,g,1.0);
 	return mix(grey, color, vec4(sat));
 }

void main(){
	
	// Look up the bloom and original base image colors.
    vec4 bloom = texture2D(u_texture, v_texCoords);
    vec4 base = texture2D(u_base, v_texCoords);
 
    // Adjust color saturation and intensity.
    bloom = adjustSaturation(bloom, BloomSaturation) * BloomIntensity;
    base = adjustSaturation(base, BaseSaturation) * BaseIntensity;
 
    // Darken down the base image in areas where there is a lot of bloom,
    // to prevent things looking excessively burned-out.
    base *= (1.0 - clamp(bloom, vec4(0.0), vec4(1.0)));
 
	gl_FragColor = v_color * (base+bloom);
	
	
}

 