package lumarca;
import processing.core.*;

public class Line {
	private static int sliceWidth;
	public float x;
	public float z;
	private float screenXPos;
	private float projectedHeight;
	private Lumarca lumarca;

	public Line(float lineX, float lineZ, float screenXPos, float projectedHeight, Lumarca lumarca) {
		this.x = lineX;
		this.z = lineZ;
		this.screenXPos = screenXPos;
		this.projectedHeight = projectedHeight;
		this.lumarca = lumarca;
	}

	/**
	* Draws on the line.  The two points can be anywhere from 0 to lumarca.max.y
	* Numbers outside these bounds get clamped to be within the bounds
	*
	* @param  y1	Value 1
	* @param  y2	Value 2
	*/
	public void draw(float y1, float y2) {
		/* TODO DEBUG */
		float top = y1 > y2 ? y1 : y2;
		float bot = top == y1 ? y2 : y1;
		float clampTop = Math.max(0, Math.min(lumarca.size.y, top));
		float clampBot = Math.max(0, Math.min(lumarca.size.y, bot));
	    float drawHeight = clampTop - clampBot;

		float rectTop = PApplet.map(clampTop,
			projectedHeight, 0,
			0, Lumarca.pApplet.height);
		float rectHeight = PApplet.map(drawHeight,
			0, projectedHeight,
			0, Lumarca.pApplet.height);

		PMatrix3D invMatrix;
		invMatrix = lumarca.getGfx().modelviewInv.get();
		invMatrix.apply(lumarca.getGfx().camera);

		Lumarca.pApplet.pushMatrix();
		Lumarca.pApplet.pushStyle();
			Lumarca.pApplet.applyMatrix(invMatrix);
			Lumarca.pApplet.noStroke();
			Lumarca.pApplet.rect(this.screenXPos, rectTop, Line.sliceWidth, rectHeight);
		Lumarca.pApplet.popStyle();
		Lumarca.pApplet.popMatrix();
	}

	public static void setSliceWidth(int inSliceWidth) {
		Line.sliceWidth = inSliceWidth;
	}

	public void renderMap(PGraphics mapBuffer) {
		int xColor = (int) PApplet.map(	this.x,
										0, lumarca.size.x,
										0, 255);
		int zColor = (int) PApplet.map(	this.z,
										0, lumarca.size.z,
										0, 255);
		for (int i = 0; i < Lumarca.pApplet.height; i++) {
			float yForI = PApplet.map(	i,
										0, Lumarca.pApplet.height,
										projectedHeight, 0);
			if (yForI <= lumarca.size.y) {
				float yColor = PApplet.map(	yForI,
											0, lumarca.size.y,
											0, 255 * 255);
				int yColor1 = (int) Math.floor(yColor / 255.0f);
		        // TODO -- this was redacted because PImage doesn't handle alpha like I want it to
				//  int yColor2 = (int) (yColor % 255.0f);
		        //int yColor2 = 255;
		        mapBuffer.stroke(xColor, yColor1, zColor);
		        mapBuffer.line(this.screenXPos, i, this.screenXPos + Line.sliceWidth, i);
			}
	    }
	}
}