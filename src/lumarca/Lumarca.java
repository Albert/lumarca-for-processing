package lumarca;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Lumarca extends ProcessingObject {
	public final static String RENDERER = "lumarca.LGraphics"; // gets called in end user's code: size(400, 400, Lumarca.RENDERER);
	public LGraphics gfx;
	public PVector max;
	public float farDepth;
	public float nearDepth;
	public ArrayList<Line> lines;

	public Lumarca(String jsonPath, PApplet p) {
		this.pApplet = p;
		/* renderer stuff */
		bindToRenderer(p);
		gfx.sphereShader = gfx.loadShader(LGraphics.class.getResource("sphere.glsl").toString());
		gfx.boxShader = gfx.loadShader(LGraphics.class.getResource("box.glsl").toString());

		/* meatspace numbers */
		JSONObject config = p.loadJSONObject(p.dataPath(jsonPath));
		float maxX = config.getJSONObject("structureSize").getFloat("x");
		float maxZ = config.getJSONObject("structureSize").getFloat("z");
		farDepth = config.getFloat("deepestProjection");
		nearDepth = farDepth - maxZ;
		float maxY = maxX * ((float) p.height / (float) p.width);
		max = new PVector(maxX, maxY, maxZ);

		/* px numbers */
		int numberOfLines = config.getJSONArray("lineDepths").size();
		int pxPerSlice = p.width / numberOfLines;
		Line.sliceWidth = pxPerSlice - config.getInt("margin");

		/* build lines */
		JSONArray lineDepths = config.getJSONArray("lineDepths");
		lines = new ArrayList<Line>();

		for (int i = 0; i < numberOfLines; i++) {
			/* z */
			float lineDepth = lineDepths.getFloat(i, 1.0000f);
			float lineZ = PApplet.map(	lineDepth,
										nearDepth, farDepth,
										0, max.z);

			/* x */
			/* x px numbers */
			float slicePxCenter = pxPerSlice * i + (pxPerSlice / 2);
			float sliceXOffset = pxPerSlice * i + (config.getInt("margin") / 2);

			/* x meatspace numbers */
			float signedXOnNearPlane = PApplet.map(	slicePxCenter,
													0, p.width,
													- max.x / 2.0f, max.x / 2.0f);

			// analogous fraction:  signedX / lineDepth = signedXOnNearPlane / nearDepth;
			float signedX = (signedXOnNearPlane / nearDepth) * lineDepth;
			float lineX = signedX + (max.x / 2.0f);

			/* y */
			// analogous fraction: projectedHeight / lineDepth = yMax / nearDepth;
			float projectedHeight = (max.y / nearDepth) * lineDepth;
			lines.add(new Line(lineX, lineZ, sliceXOffset, projectedHeight, this));
		}
		
		/* find or build map */
		boolean generateNewMap = false;
		String mapKey = config.getString("ignoreThis", "undefined");
		if (mapKey == "undefined") {
			generateNewMap = true;
		} else {
			File f = new File(p.dataPath(mapKey + ".png"));
			if (!f.exists()) {
				generateNewMap = true;
			}
		}

		if (generateNewMap) {
			Random r = new Random();
			StringBuffer sb = new StringBuffer();
			while(sb.length() < 16){
				sb.append(Integer.toHexString(r.nextInt()));
			}
			mapKey = sb.toString().substring(0, 15);
			config.setString("ignoreThis", mapKey);
			p.saveJSONObject(config, p.dataPath(jsonPath));
			PGraphics mapBuffer = p.createGraphics(p.width, p.height);
			mapBuffer.beginDraw();
			mapBuffer.noSmooth();
			for (int i = 0; i < lines.size(); i++) {
				lines.get(i).renderMap(mapBuffer);
			}
			mapBuffer.endDraw();
			mapBuffer.save(p.dataPath(mapKey + ".png"));
		}

		gfx.mapData = p.loadImage(p.dataPath(mapKey + ".png"));
	}

	private void bindToRenderer(PApplet p) {
		gfx = (LGraphics) p.g;
		gfx.lumarca = this;
	}
}