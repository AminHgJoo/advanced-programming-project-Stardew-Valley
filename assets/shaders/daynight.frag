#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform float     u_nightFactor;   // 0 = day, 1 = full night

void main() {
    vec4 color     = texture2D(u_texture, v_texCoord) * v_color;
    vec3 nightTint = vec3(0.1, 0.1, 0.2);
    vec3 blended   = mix(color.rgb, color.rgb * nightTint, u_nightFactor);
    gl_FragColor   = vec4(blended, color.a);
}
