package xyz.jpenilla.squaremapstitcher

import java.io.File

private const val correctSyntax = "Invalid arguments. Correct syntax: <path_to_tiles_dir> <destination> [--verbose]"

fun main(args: Array<String>) {
    require(args.size == 2 || args.size == 3) { correctSyntax }
    val tiles = File(args[0])
    val dest = File(args[1])
    val verbose = if (args.size == 3) {
        if (args[2] == "--verbose") {
            true
        } else {
            error(correctSyntax)
        }
    } else {
        false
    }
    Stitcher.run(tiles.toPath(), dest.toPath(), verbose)
}
