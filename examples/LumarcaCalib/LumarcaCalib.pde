/*
This Lumarca calibration routine is for is you want to build a static structure and then set the projector

Strings closest to the projector are Red
Strings furthest from the projector are Blue
Strings in between are Green

This way takes lots of patients and nudging
*/

import lumarca.*;

public Lumarca thisLumarca;

 void setup() {
  size(1024, 768, "lumarca.LGraphics");
  thisLumarca = new Lumarca("lumarcaConfig.json", this);
}

public void draw() {
  background(0);

  for (int i = 0; i < thisLumarca.getLineCount(); i++) {
    Line l = thisLumarca.getLine(i);
    if (l.z < thisLumarca.size.z * .33333) {
      fill(255, 0, 0);
    } else if (l.z < thisLumarca.size.z * .6666) {
      fill(0, 255, 0);
    } else {
      fill(0, 0, 255);
    }
    l.draw(0, thisLumarca.size.y);
  }
}
