uniform float radius;
uniform vec2 screenSize;
uniform sampler2D mapData;
uniform mat4 invMatrix;
uniform vec4 fill;
uniform vec3 xyzMax;

vec4 mapColor;

float map( in vec3 p ) {
  return (length(p)-radius) / radius;
}

vec3 pxLocation(void) {
  vec3 mapValue = vec3(mapColor.r, (mapColor.g + mapColor.a / 255.0), mapColor.b);
  vec4 pxLocation = vec4(mapValue * xyzMax.xyz, 1.);
  vec4 o = pxLocation * invMatrix;
  return o.xyz;
}

void main(void) {
  mapColor = texture2D(mapData, vec2(gl_FragCoord.x, screenSize.y -gl_FragCoord.y) / screenSize).rgba;
  if (mapColor != vec4(0., 0., 0., 0.)) {
    float grade = map(pxLocation());
    if (grade < 0.) {
      gl_FragColor = vec4(fill.rgba);
      return;
    }
  }
}