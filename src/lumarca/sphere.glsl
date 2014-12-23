/*

I guess what's interesting here is that there is no transformation matrices.

In other words, the shader builds things in the following:

x: -resX/2., resX/2.
y: -resY/2., resY/2.
z: -resY/2., resZ/2.
x, right
y, up
z, forward

birds eye:


     _______
\   |       |   /          z = resZ/2
 \  |       |  /
  \ |       | /
   \|_______|/             z = -resZ/2
x=-resX/2   x=resX/2



         <--- camera position (cameraZ)


radius: defined in pixel units

*/

uniform vec3 screenRes;//
uniform float pxPerString;//
uniform float marginSize;//
uniform float stringCount;//
uniform float cameraZ;//
uniform float radius;
uniform sampler2D lumarcaMap;
uniform mat4 invMatrix;
uniform vec4 fill;
//uniform vec4 shellColor;
//uniform float shellWeight;

vec4 pxLocation(void) {

  // determine depth of current string as per iChannel0
  float currString = floor(gl_FragCoord.x / pxPerString);
  float depthFactor = texture2D(lumarcaMap, vec2(currString/stringCount, 0.0)).r;
  float depth = (depthFactor * screenRes.z) - screenRes.z/2.;

  // vec3 pixelLocation = gl_FragCoord.xy
  vec3 zeroedFragCoord = vec3(gl_FragCoord.xy - screenRes.xy/2., -screenRes.z/2.);
  vec3 cameraPosition = vec3(0., -screenRes.y/2., cameraZ);
  vec3 angleToPx = normalize(vec3(zeroedFragCoord) - cameraPosition);

  // pxLocation.xy : angleToPx.xy :: depth - cameraZ : angleToPx.z
  vec4 pxLocation = vec4(angleToPx.xy * (depth - cameraZ) / angleToPx.z, depth, 1.);
  pxLocation = pxLocation - vec4(0., screenRes.y/2., 0., 0.);
  vec4 o = pxLocation * invMatrix;
  return o;
}


float map( in vec3 p ) {
  return (length(p)-radius) / radius;
}

void main(void) {


  // If this is in a gutter, do not display & return out of main()
  if (mod(gl_FragCoord.x - marginSize/2.0, pxPerString) >= pxPerString - marginSize) {
    gl_FragColor = vec4(0, 0, 0, 1);
    return;
  }

  float grade = map(pxLocation().xyz);
  //float shellTest1 = map(pxLocation().xyz + vec3(0, shellWeight, 0)) - 1.;
  //float shellTest2 = map(pxLocation().xyz - vec3(0, shellWeight, 0)) - 1.;

  if (grade > 1.) {
    gl_FragColor = vec4(0, 0, 0, 0);
    return;
  }

  gl_FragColor = vec4(fill.rgb, fill.a * grade);
  return;

}
