package lumarca;
import processing.core.*;

public class Line {
	public static int sliceWidth;
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

	public void draw(float y1, float y2) {
		float top = y1 > y2 ? y1 : y2;
		float bot = top == y1 ? y2 : y1;
		float clampTop = Math.max(0, Math.min(lumarca.max.y, top));
		float clampBot = Math.max(0, Math.min(lumarca.max.y, bot));
	    float drawHeight = clampTop - clampBot;

		float rectTop = PApplet.map(clampTop,
			lumarca.max.y, 0,
			0, lumarca.pApplet.height);
		float rectHeight = PApplet.map(drawHeight,
			0, lumarca.max.y,
			0, lumarca.pApplet.height);

		PMatrix3D invMatrix;
		invMatrix = lumarca.gfx.modelviewInv.get();
		invMatrix.apply(lumarca.gfx.camera);

		lumarca.pApplet.pushMatrix();
		lumarca.pApplet.pushStyle();
			lumarca.pApplet.applyMatrix(invMatrix);
			lumarca.pApplet.noStroke();
			lumarca.pApplet.rect(this.screenXPos, rectTop, Line.sliceWidth, rectHeight);
		lumarca.pApplet.popStyle();
		lumarca.pApplet.popMatrix();
	}

	public void renderMap(PGraphics mapBuffer) {
		int xColor = (int) PApplet.map(	this.x,
										0, lumarca.max.x,
										0, 255);
		int zColor = (int) PApplet.map(	this.z,
										0, lumarca.max.z,
										0, 255);
		for (int i = 0; i < lumarca.pApplet.height; i++) {
			float yForI = PApplet.map(	i,
										0, lumarca.pApplet.height,
										projectedHeight, 0);
			if (yForI <= lumarca.max.y) {
				float yColor = PApplet.map(	yForI,
											0, lumarca.max.y,
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