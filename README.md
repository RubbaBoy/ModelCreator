# ModelCreator
Creates a .json model for a Minecraft resourcepack from any .schematic file. It does it as cleanly and efficiently as possible, by combining blocks of the same type, and using textures matching with the color of the block.


## Features
The program currently supports the following blocks:
- Wool
- Glass
- Glass Panes
- Hardened Clay
- Concrete
- Concrete Powder

By default, the colors of the block will use the texture of the following names:
- blocks/white
- blocks/orange
- blocks/magenta
- blocks/light_blue
- blocks/yellow
- blocks/lime
- blocks/pink
- blocks/gray
- blocks/light_gray
- blocks/cyan
- blocks/purple
- blocks/blue
- blocks/brown
- blocks/green
- blocks/red
- blocks/black

This is to reduce lag when there is a lot of textures of different types, and to prevent distortion of normal blocks' images across larger surfaces.

If you want support for more blocks, just look in the class `com.uddernetworks.modelcreator.main.MappedTextures`, and add stuff from there. Same goes with texture names, just look in the `Texture` enum in the same class, it's not rocket science. You will also need to edit the template.json in the reosurces folder to match your texture changes.

## Usage

Run the jar with the first argument being the schematic file, and second argument being the output json file (The file will be created/overwritten).

Example:
```
java -jar ModelCreator-1.0-SNAPSHOT-all.jar "schematics\test_schematic.schematic" output.json
```

## Example Schematic
Example of a 16x16x16 schematic (On the left) being converted directly to a model (On the right, on an invisible armor stand). The convertion to the model from schematic took 145ms.

![Example model](https://rubbaboy.me/images/hyw240d)
