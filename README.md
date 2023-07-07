# squaremap-stitcher

Stitches squaremap tile images together.

### Build from source
- Have a JDK 17 or newer installed
- Clone or otherwise download this repository
- Execute `./gradlew build` inside the project directory
- Built fat jar will be at `./build/libs/squaremap-stitcher-all.jar`

### Use
- `java -jar squaremap-stitcher-all.jar <path_to_tiles_dir> <destination>`
  - `<path_to_tiles_dir>`: path to a squaremap tiles directory. These directories are located at `./web/tiles/<world>/<zoom>`
    within the squaremap directory.
    A larger zoom number corresponds to a more zoomed in image, with the highest zoom being 1:1 block to pixel.
    So for the highest quality stitch, you should pick the most zoomed in directory.
  - `<destination>`: path to the destination png, for example `stitched.png` to output to `stitched.png` in the current
    working directory. Will be overwritten!
