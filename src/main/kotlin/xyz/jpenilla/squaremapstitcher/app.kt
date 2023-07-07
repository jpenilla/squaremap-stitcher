package xyz.jpenilla.squaremapstitcher

import java.io.File

fun main(args: Array<String>) {
    require(args.size == 2) {
        "Expected two arguments, the directory containing tile images, and the destination image. Got: ${args.contentToString()}"
    }
    val tiles = File(args[0])
    val dest = File(args[1])
    Stitcher.run(tiles.toPath(), dest.toPath())
}
