import lumarca.*;

public Lumarca thisLumarca;

public void setup() {
  //size(displayWidth, displayHeight, P3D);
  size(displayWidth, displayHeight, Lumarca.RENDERER);
  thisLumarca = new Lumarca("lumarcaConfig.json", this);
  blendMode(BLEND);
}

public void draw() {
  background(0);
  fill(0, 255, 0, 255);
  translate(0f, 200f, 0f);
  rotate(1.0f, 1.0f, 0f, (float) millis() / 4000.0f);
  box(400.0f);
  fill(255, 0, 0);
}
