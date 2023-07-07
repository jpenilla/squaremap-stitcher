package xyz.jpenilla.squaremapstitcher

import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.nio.file.Path
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.outputStream

object Stitcher {
    private const val size = 512

    fun run(tilesDir: Path, dest: Path) {
        val tiles = tilesDir.listDirectoryEntries("*.png").map { TileImage.from(it) }
        val minX = tiles.minOf { it.position.x }
        val maxX = tiles.maxOf { it.position.x }
        val widthTiles = maxX - minX + 1
        val minZ = tiles.minOf { it.position.z }
        val maxZ = tiles.maxOf { it.position.z }
        val heightTiles = maxZ - minZ + 1

        val stitched = BufferedImage(size * widthTiles, size * heightTiles, BufferedImage.TYPE_INT_ARGB)

        for (tile in tiles) {
            val pos = tile.position
            val startX = (pos.x - minX) * size
            val startZ = (pos.z - minZ) * size
            println("Processing ${tile.path}...")
            val tileImg = ImageIO.read(tile.path.toFile())
            for (dX in 0 until size) {
                for (dZ in 0 until size) {
                    val rgb = tileImg.getRGB(dX, dZ)
                    stitched.setRGB(startX + dX, startZ + dZ, rgb)
                }
            }
        }

        dest.parent.createDirectories()
        dest.deleteIfExists()
        BufferedOutputStream(dest.outputStream()).use { s ->
            save(stitched, s)
        }
        println("Done! Saved stitched image to $dest")
    }

    private fun save(image: BufferedImage, out: OutputStream) {
        val writer = ImageIO.getImageWritersByFormatName("png").next()
        ImageIO.createImageOutputStream(out).use { imageOutputStream ->
            writer.output = imageOutputStream
            val param = writer.defaultWriteParam
            if (param.canWriteCompressed()) {
                param.compressionMode = ImageWriteParam.MODE_EXPLICIT
                if (param.compressionType == null) {
                    param.compressionType = param.compressionTypes[0]
                }
                param.compressionQuality = 0.0f
            }
            writer.write(null, IIOImage(image, null, null), param)
        }
    }
}

data class TileImage(val path: Path, val position: TilePosition) {
    companion object {
        fun from(path: Path): TileImage {
            return TileImage(path, TilePosition.parse(path.fileName.toString().substringBefore(".png")))
        }
    }
}

data class TilePosition(val x: Int, val z: Int) {
    companion object {
        fun parse(pos: String): TilePosition {
            val split = pos.split("_")
            return TilePosition(split[0].toInt(), split[1].toInt())
        }
    }
}
