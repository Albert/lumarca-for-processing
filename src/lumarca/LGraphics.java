package lumarca;

import processing.core.*;
import processing.opengl.*;

public class LGraphics extends PGraphicsOpenGL {

	public float resZ;
	public float cameraZ;
	public float pxPerUnit;
	public Lumarca lumarca;
	public PShader sphereShader;
	public PShader boxShader;
	public PImage lumarcaData;

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

	@Override
	public void sphere(float r) {
		buildShape(sphereShader, r);
	}

	@Override
	public void box(float r) {
		buildShape(boxShader, r);
	}

	private void buildShape(PShader myShader, float r) {
		myShader.set("lumarcaMap", lumarcaData);
		myShader.set("stringCount", (float) lumarca.numOfStrings);
		myShader.set("cameraZ", cameraZ);
		myShader.set("marginSize", (float) lumarca.marginSize);
		myShader.set("screenRes", (float) width, (float) height, resZ);
		myShader.set("pxPerString", (float) width / (float) lumarca.numOfStrings);
		PStyle s = getStyle();
		colorCalc(s.fillColor);
		myShader.set("fill", calcR, calcG, calcB, calcA);

		//colorCalc(s.strokeColor);
		//myShader.set("shellColor", calcR, calcG, calcB, calcA);

		PMatrix3D invMatrix;
		invMatrix = modelviewInv.get();
		invMatrix.apply(camera);
		myShader.set("invMatrix", invMatrix);
		myShader.set("radius", r);
		//myShader.set("shellWeight", s.strokeWeight);

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
