import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage



class WorkImage(val path_image: String, var offset_x: Int = 0, var offset_y: Int = 0) {
    lateinit var Picture: BufferedImage
    lateinit var PictureNew: BufferedImage
    var width: Int
    var height: Int

    var widthFragment: Int = 0
    var heightFragment: Int = 0

    var firstFragmentX: Int = 0
    var firstFragmentY: Int = 0


    var pixelsFor: ArrayList<Pair<Int, Int>>
    var pixelsInner: ArrayList<Pair<Int, Int>>

    init {
        println(File(path_image))
        Picture = ImageIO.read(File(path_image))
        width = Picture.width
        height = Picture.height
        println("$width, $height")

        pixelsFor = ArrayList()
        pixelsInner = ArrayList()
    }

    fun paint() {
        PictureNew = BufferedImage(widthFragment, heightFragment, BufferedImage.TYPE_INT_ARGB)
        pixelsFor.forEach {
            try {
                PictureNew.setRGB(
                    it.first - firstFragmentX + offset_x,
                    it.second - firstFragmentY + offset_y,
                    Picture.getRGB(it.first, it.second)
                )
            }catch (exp: Exception) {
                println("${it.first - firstFragmentX + offset_x}, ${it.second - firstFragmentY + offset_y}")
                throw Exception("Out of Image OUT")
            }
        }

        pixelsInner.forEach {
            try{
                PictureNew.setRGB(
                    it.first - firstFragmentX + offset_x,
                    it.second - firstFragmentY + offset_y,
                    RGBtoInt(0,0,0, 0)
                )
            }catch (exp: Exception) {
                println("${it.first - firstFragmentX + offset_x}, ${it.second - firstFragmentY + offset_y}")
                throw Exception("Out of Image IN")
            }
        }
    }

    fun save(number: String) {
        val output = File(DIRECTORY_TO + number + ".png")
        ImageIO.write(PictureNew, "png", output)

    }

    fun RGBtoInt(r: Int, g: Int, b: Int, a: Int) = (a shl 24 or (r shl 16) or (g shl 8) or b)
}