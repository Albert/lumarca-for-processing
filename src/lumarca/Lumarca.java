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
	public PVector size;
	private LGraphics gfx;
	private float farDepth;
	private float nearDepth;
	private ArrayList<Line> lines;

	/**
	* <strong>Builds a new Lumarca</strong>
	* 
	* <p>* Requires a json config file.  Configurations are as follows:</p>
	* 
	* <ul>
	*   <li>"structureSize" refers to the size of the render field in real units*. X is width, Z is depth. You can ignore Y (height), as this will be calculated based on X and aspect ratio.</li>
	*   <li>"deepestProjection" is the distance in real units* from the projector's focal point to the far end of the render field.</li>
	*   <li>"margin" is the number of black pixels between each column of pixels. There currently is a bug where only even numbers work here</li>
	*   <li>"lineDepths" describes the arrangement of the string. The floats describe the Z coordinate of all the strings. The array is sorted from left-most column of pixels to right-most column of pixels.</li>
	*   <li>"ignoreThis" can be ignored completely</li>
	* </ul>
	* 
	* <p>* Real units are like "inches" or "centimeters".  Whatever you use should stay consistent.</p>
	* 
	* <p>If you make updates to the config file, you should delete the "map" file, which is a .png that gets generated in the data folder with a name like 69dd6a25aa04f26.png.</p>
	* 
	* @param  jsonPath	Name of the config file
	* @param  p			the applet you are pointing it at (usually "this")
	*/
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
		size = new PVector(maxX, maxY, maxZ);

		/* px numbers */
		int numberOfLines = config.getJSONArray("lineDepths").size();
		int pxPerSlice = p.width / numberOfLines;
		Line.setSliceWidth(pxPerSlice - config.getInt("margin"));

		/* build lines */
		JSONArray lineDepths = config.getJSONArray("lineDepths");
		lines = new ArrayList<Line>();

		for (int i = 0; i < numberOfLines; i++) {
			/* z */
			float lineZ = lineDepths.getFloat(i);
			float lineFromProjector = lineZ + nearDepth;

			/* x */
			/* x px numbers */
			float slicePxCenter = pxPerSlice * i + (pxPerSlice / 2);
			float sliceXOffset = pxPerSlice * i + (config.getInt("margin") / 2);

			/* x meatspace numbers */
			float signedXOnNearPlane = PApplet.map(	slicePxCenter,
													0, p.width,
													- size.x / 2.0f, size.x / 2.0f);

			// analogous fraction:  signedX / lineDepth = signedXOnNearPlane / nearDepth;
			float signedX = (signedXOnNearPlane / nearDepth) * lineFromProjector;
			float lineX = signedX + (size.x / 2.0f);

			/* y */
			// analogous fraction: projectedHeight / lineDepth = yMax / nearDepth;
			float projectedHeight = (size.y / nearDepth) * lineFromProjector;
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

	public LGraphics getGfx() {
		return gfx;
	}

	public Line getLine(int index) {
		return lines.get(index);
	}

	public int getLineCount() {
		return lines.size();
	}

	private void bindToRenderer(PApplet p) {
		gfx = (LGraphics) p.g;
		gfx.lumarca = this;
	}
}