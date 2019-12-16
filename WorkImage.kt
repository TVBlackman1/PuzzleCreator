import java.awt.image.DataBufferByte
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage



class WorkImage(val path_image: String, var offset_x: Int = 0, var offset_y: Int = 0) {
    lateinit var Picture: BufferedImage // исходное изображение
    lateinit var PictureNew: BufferedImage // изображение фрагмента
    lateinit var PictureBorder: BufferedImage // изображение обводки
    var width: Int
    var height: Int

    // размеры фрагмента
    var widthFragment: Int = 0
    var heightFragment: Int = 0

    // координаты старта фрагмента на исходном изображении
    var firstFragmentX: Int = 0
    var firstFragmentY: Int = 0


    var pixelsFor: ArrayList<Pair<Int, Int>> // пиксели, необходимые отобразить
    var pixelsInner: ArrayList<Pair<Int, Int>> // пиксели, необходимые убрать (для выемок)
    var pixelsBorder: ArrayList<Pair<Int, Int>> // пиксели обводки

    init {
//        println(File(path_image))
        Picture = ImageIO.read(File(path_image))
        width = Picture.width
        height = Picture.height
        println("$width, $height")

        pixelsFor = ArrayList()
        pixelsInner = ArrayList()
        pixelsBorder = ArrayList()
    }

    fun paint() {
        PictureNew = BufferedImage(widthFragment, heightFragment, BufferedImage.TYPE_INT_ARGB)
        PictureBorder = BufferedImage(widthFragment, heightFragment, BufferedImage.TYPE_INT_ARGB)
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
        pixelsBorder.forEach {
            try{
                PictureBorder.setRGB(
                    it.first - firstFragmentX + offset_x,
                    it.second - firstFragmentY + offset_y,
                    RGBtoInt(0,0,0, 255)
                )
            }catch (exp: Exception) {
                println("${it.first - firstFragmentX + offset_x}, ${it.second - firstFragmentY + offset_y}")
                throw Exception("Out of Image IN")
            }
        }
    }

    fun save(number: String) {
        var output = File(DIRECTORY_TO + number + ".png")
        ImageIO.write(PictureNew, "png", output)
        output = File(DIRECTORY_TO + number + "_.png")
        ImageIO.write(PictureBorder, "png", output)

    }

    fun RGBtoInt(r: Int, g: Int, b: Int, a: Int) = (a shl 24 or (r shl 16) or (g shl 8) or b)
}