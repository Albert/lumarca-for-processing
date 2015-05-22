Intro
=====

[Installation instructions and JavaDoc](http://lumarca.info/code/)

[Lumarca](http://lumarca.info/) is a volumetric display that creates digital 3d images in real 3d space.

[Processing](https://processing.org/) is a simplified Java environment for creative coders.

This library will render your 3d Processing scenes in the Lumarca.


Usage
=====
If you are writing a 3d Processing sketch, and have this:

```
void setup() {
  size(1024, 768, P3D);
}

void draw() {
  /* lost of other processing stuff you want in here */
  sphere(60);
}
```

You can make some minor adjustments to render it in a Lumarca:

```
import lumarca.*;
public Lumarca thisLumarca;

void setup() {
  size(1024, 768, Lumarca.renderer);
  thisLumarca = new Lumarca("lumarcaConfig.json", this);
}

void draw() {
  /* lost of other processing stuff you want in here */
  sphere(60);
}
```

Config File
-----------

There is an example config file in the example projects.  Here's some further explanation on those values:

**structureSize** refers to the size of the render field in real units*.  X is width, Z is depth.  You can ignore Y (height), as this will be calculated based on X and aspect ratio.

**deepestProjection** is the distance in real units* from the projector's focal point to the far end of the render field.

**margin** is the number of black pixels between each column of pixels.  There currently is a bug where only even numbers work here

**lineDepths** describes the arrangement of the string.  The floats describe the Z coordinate of all the strings.  The array is sorted from left-most column of pixels to right-most column of pixels.

**ignoreThis** can be ignored completely

*A note about real units -- for the config and for the functions that follow, you will code in real units.  This can be inches or centimeters, but whatever you choose you should stay consistent.


What Processing Features are supported?
---------------------------------------

Processing features that aren't graphics-related are fully supported.  This includes things like mouse, key, data manipulation, string handling, sound, etc...

Some graphics functions are supported.  The following will work as you might expect:
* sphere()
* box()
* all matrix transforms (translate, scale, rotate, etc)
* fill(), including alpha support
* background()

The following are not supported and will not be built into this library as they aren't really relevant to volumetric rendering:
* textures
* lighting
* camera

Features I'd like to build
--------------------------
Here are some features that I'd like to build into the project

* better support for different types of construction configurations (one per depth) including
  * calibration patterns that facilitate different construction techniques
  * string location generators
* stroke and all stroke functions (like color, weight) to be used as a way to draw surface pixels of geometries
* custom shaders to create your own volumetric effects
* convenience methods for the following:
  * orientation definition (y-up, y-down, z-up)
  * setting origin at center or at corner
  * setting units of measure as real units, normalized 0 to 1, or pixel approximations
* decouple the initializer from the renderer so that you can have a Lumarca object while still using P3D


Links
---------------------------------
[Totally Nerdy and non-sessential geek out on why this library is so cool](http://albert-hwang.com/2015/03/lumarca-for-processing/)
