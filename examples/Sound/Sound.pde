import lumarca.*;
import ddf.minim.*;

public Lumarca thisLumarca;
Minim minim;
AudioInput in;

public void setup() {
  //size(displayWidth, displayHeight, P3D);
  size(displayWidth, displayHeight, Lumarca.RENDERER);
  thisLumarca = new Lumarca("lumarcaConfig.json", this);
  
  minim = new Minim(this);
  in = minim.getLineIn();
}

public void draw() {
  background(0);

  fill(0, 255, 0, 255);
  
  pushMatrix();
    translate(0f, 200f, 0f);
    rotate(1.0f, 1.0f, 0f, (float) millis() / 4000.0f);
    box(400 + (in.left.get(20) * 1000));
  popMatrix();

  fill(255, 255, 255, 255);  
  stroke(255, 255, 255, 255);
  for(int i = 0; i < in.bufferSize() - 1; i++) {
    line( i, 50 + in.left.get(i)*50, i+1, 50 + in.left.get(i+1)*50 );
    line( i, 150 + in.right.get(i)*50, i+1, 150 + in.right.get(i+1)*50 );
  }

}


