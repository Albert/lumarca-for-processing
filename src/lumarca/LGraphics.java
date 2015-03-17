package lumarca;

import processing.core.*;
import processing.opengl.*;

public class LGraphics extends PGraphicsOpenGL {

	public Lumarca lumarca;
	public PShader sphereShader;
	public PShader boxShader;
	public PImage mapData;

	public LGraphics() {
		super();
	}

	public static void main(String[] args) {
	}
	//
	//@Override
	//public void pushMatrix() {
	//}
	//
	//@Override
	//public void resetMatrix() {
	//}
	//
	//@Override
	//public void popMatrix() {
	//}
	//
	//@Override
	//public void blendMode(int mode) {
	//}
	//
	//@Override
	//public void background(int rgb) {
	//}

	/**
	* Draw a sphere.
	*
	* @param  r		radius
	*/

	@Override
	public void sphere(float r) {
		buildShape(sphereShader, r);
	}

	/**
	* Draw a box.
	*
	* @param  r		radius
	*/

	@Override
	public void box(float r) {
		buildShape(boxShader, r);
	}

	private void buildShape(PShader myShader, float r) {
		// lumarca geometry
		myShader.set("xyzMax", lumarca.size);
		myShader.set("screenSize", (float) width, (float) height);
		myShader.set("mapData", mapData);

		// styling and geo
		PStyle s = getStyle();
		colorCalc(s.fillColor);
		myShader.set("fill", calcR, calcG, calcB, calcA);
		//colorCalc(s.strokeColor);
		//myShader.set("shellColor", calcR, calcG, calcB, calcA);
		myShader.set("radius", r);
		//myShader.set("shellWeight", s.strokeWeight);

		// matrix transforms
		PMatrix3D invMatrix = modelviewInv.get();
		invMatrix.apply(camera);
		myShader.set("invMatrix", invMatrix);

		shader(myShader);

		pushMatrix();
		pushStyle();
			noStroke();
			fill(255, 255);
    		applyMatrix(invMatrix);
        	rect(0, 0, width, height);
        	resetShader();
    	popStyle();
    	popMatrix();
	}

}
