attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projModelView;
varying vec4 v_color;

void main()
{
   v_color =  a_color;
   v_color.a = v_color.a * (256.0/255.0);
   gl_Position =  u_projModelView *  a_position;
}

