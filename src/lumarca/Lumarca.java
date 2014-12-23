package lumarca;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Lumarca extends ProcessingObject {
	public final static String RENDERER = "lumarca.LGraphics"; // gets called in end user's code: size(400, 400, Lumarca.RENDERER);
	private LGraphics g;
	public float xRealUnits;
	public float zRealUnits;
	public float furthestProjection;
	public float[] stringZLocations;
	public int numOfStrings; // should just be a length of one of em flexi size arrays
	public int marginSize; // should default to one quarter of the draw size, but be settable

	public Lumarca(String jsonPath, PApplet p) {
		coupleToRenderer(p);

		JSONObject config = p.loadJSONObject(jsonPath);
		xRealUnits = config.getFloat("x real units");
		zRealUnits = config.getFloat("z real units");
		furthestProjection = config.getFloat("furthest projection");

		marginSize = config.getInt("margin size");
		JSONArray floatList = config.getJSONArray("string z locations");
		stringZLocations = floatList.getFloatArray();
		numOfStrings = stringZLocations.length;
		/* resZ / resX, aka width, as zRealUnits / xRealUnits*/
		g.resZ = zRealUnits * ((float) p.width / xRealUnits);
		float cameraZRealUnits = - furthestProjection + zRealUnits/2.0f;
		g.cameraZ = cameraZRealUnits * ((float) p.width / xRealUnits);
		g.pxPerUnit = (float) p.width / xRealUnits;
		g.lumarcaData = p.loadImage(".lumarcaData.png"); // todo, factor out -- too expensive
		g.sphereShader = g.loadShader(LGraphics.class.getResource("sphere.glsl").toString());
		g.boxShader = g.loadShader(LGraphics.class.getResource("box.glsl").toString());
		if (p.createInput(".lumarcaData.png") == null) {
			makeDepthTex();
		}
	}

	private void coupleToRenderer(PApplet p) {
		g = (LGraphics) p.g;
		g.lumarca = this;
	}

	private void makeDepthTex() {
		for (int i = 0; i < numOfStrings; i ++) {
			int distanceAlong = 0; // 0 to 255
			distanceAlong = (int) (255.0f * (stringZLocations[i] - furthestProjection + zRealUnits) / zRealUnits);
			pApplet.stroke(distanceAlong);
			pApplet.point(i, 0);
		}
		
		PImage img = pApplet.get(0, 0, numOfStrings, 1);
		img.save("data/.lumarcaData.png");
	}
}