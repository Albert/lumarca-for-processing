
import lumarca.*;

public Lumarca thisLumarca;

void setup() {
  size(1024, 768, "lumarca.LGraphics");
  thisLumarca = new Lumarca("lumarcaConfig.json", this);
}

public void draw() {
  background(0);
  translate(thisLumarca.size.x / 2, thisLumarca.size.y / 2, thisLumarca.size.z / 2);
  fill(255, 0, 0);
  sphere(5);
  translate(10, 0, 0);
  rotateX(float(millis()) / 3000.0 );
  fill(0, 255, 0);
  box(3);
}
