import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

// Численные значения, полученные из графика функции
const val DX = 1.4
const val FUNC_LENGHT = 3.767
const val FUNC_WIDTH = 1.64 * 2

const val FR_RANDOM = 25

class Fragment(val rect: Rect,
               val fr_rotate: Boolean,
               val leftFr: Fragment? = null,
               val topFr: Fragment? = null,
               val lastRight: Boolean = false,
               val lastBottom: Boolean = false
) {

    var fragment_width: Int
    var fragment_height: Int
    var ledge_length: Int

    var third_width: Int
    var third_height: Int

    var func_scale: Double

    lateinit var a: Point
    lateinit var b: Point
    lateinit var c: Point
    lateinit var d: Point

    val right_mode: Pair<Boolean, Int>?
    val bottom_mode: Pair<Boolean, Int>?
    var max_size: Int

    lateinit var pixels: ArrayList<Pair<Int, Int>>
    lateinit var inner_pixels: ArrayList<Pair<Int, Int>>
    lateinit var border_pixels: ArrayList<Pair<Int, Int>>

    init {

        print("Fragment ")
        if(lastRight) println()

        a = rect.a
        b = rect.b
        c = rect.c
        d = rect.d
        fragment_height = c.y - b.y
        fragment_width = b.x - a.x

        third_width = fragment_width / 3
        third_height = fragment_height / 3
        ledge_length = min(third_width, third_height)
        func_scale = ledge_length / FUNC_LENGHT * PART // можно доработать потом (см. питон)
        max_size = Math.ceil(FUNC_LENGHT * func_scale).toInt()
        pixels = ArrayList()
        inner_pixels = ArrayList()
        border_pixels = ArrayList()

        rect_pixels()
        top()
        left()
        right_mode = right()
        bottom_mode = bottom()

    }

    fun getRandomOffset(): Int {
        val margin = FR_RANDOM / 2
        val ret = (Random().nextInt(FR_RANDOM - margin * 2) + margin)
        return ret
    }

    fun rect_pixels() {
        for (x in a.x until  b.x) {

            var borderX = false
            if ((Math.abs(x - a.x) <= borderWidth) or (Math.abs(x - b.x) <= borderWidth)) {
                borderX = true
            }

            for (y in a.y until d.y) {

                var borderY = false
                if ((Math.abs(y - a.y) <= borderWidth) or (Math.abs(y - d.y) <= borderWidth)) {
                    borderY = true
                }
                if(borderX or borderY) {
                    border_pixels.add(x to y);
                }

                pixels.add(x to y);
            }

        }

    }


    fun left() {
        if (leftFr == null)
            return
        if (leftFr.right_mode == null)
            return
        val (mode, offset) = leftFr.right_mode
        if (!mode) {
            for (x in (a.x - third_width) until (a.x))
                for (y in (b.y + offset) until (b.y + offset + third_height)) {
                    val tmp = left_function(x - a.x, y - b.y - offset)
                    if (tmp.first)
                        pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        } else {
            for (x in (a.x) until (a.x + third_width))
                for (y in (b.y + offset) until (b.y + offset + third_height)) {
                    val tmp = right_function(x - a.x, y - b.y - offset)
                    if (tmp.first)
                        inner_pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        }
    }

    fun right(): Pair<Boolean, Int>?{
        if(lastRight) return null

        val rnd = getRandomOffset()
        var rnd_offset = ((2 * third_height - FUNC_WIDTH * func_scale) * rnd / FR_RANDOM).toInt()
        if(fr_rotate) rnd_offset += third_height

        val mode = Random().nextBoolean()
        if (mode) {
            for (x in b.x until (b.x + third_width))
                for (y in (b.y + rnd_offset) until (b.y + rnd_offset + third_height)) {
                    val tmp = right_function(x - b.x, y - b.y - rnd_offset)
                    if (tmp.first)
                        pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        } else {
            for (x in (b.x - third_width) until (b.x))
                for (y in (b.y + rnd_offset) until (b.y + rnd_offset + third_height)) {
                    val tmp = left_function(x - b.x, y - b.y - rnd_offset)
                    if (tmp.first)
                        inner_pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        }
        return mode to rnd_offset
    }

    fun bottom(): Pair<Boolean, Int>? {
        if(lastBottom) return null

        val rnd = getRandomOffset()
        var rnd_offset = ((2 * third_width - FUNC_WIDTH * func_scale) * rnd / FR_RANDOM).toInt()
        if(!fr_rotate) rnd_offset += third_width

        val mode = Random().nextBoolean()
        if (mode) {
            for (x in (d.x + rnd_offset) until (d.x + 2 * third_width + rnd_offset))
                for (y in (d.y) until (d.y + third_height)) {
                    val tmp = top_function(x - d.x - rnd_offset, y - d.y)
                    if (tmp.first)
                        pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        } else {
            for (x in (d.x + rnd_offset) until (d.x + 2 * third_width + rnd_offset))
                for (y in (d.y - third_height) until (d.y)) {
                    val tmp = bottom_function(x - d.x - rnd_offset, y - d.y)
                    if (tmp.first)
                        inner_pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y)
                    }
                }
        }
        return mode to rnd_offset
    }

    fun top() {
        if(topFr == null)
            return
        if (topFr.bottom_mode == null)
            return
        val (mode, offset) = topFr.bottom_mode
        if (!mode) {
            for (x in (d.x + offset) until (d.x + third_width + offset))
                for (y in (a.y - third_height) until (a.y)) {
                    val tmp = bottom_function(x - d.x - offset, y - a.y)
                    if (tmp.first)
                        pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        } else {
            for (x in (d.x + offset) until (d.x + third_width + offset))
                for (y in (a.y) until (a.y + third_height)) {
                    val tmp = top_function(x - d.x - offset, y - a.y)
                    if (tmp.first)
                        inner_pixels.add(x to y)
                    if(tmp.second) {
                        border_pixels.add(x to y);
                    }
                }
        }
    }

    fun right_function(x: Int, y: Int): Pair<Boolean, Boolean> {
        // возвращает истину, если это часть изображения
        // возвращает истину, если это часть обводки

        val f = (6 - (x / func_scale - DX).pow(3) + (x / func_scale - DX) * 3.07) / 3
        if (f > 0) {
            var second = false;
            if(
                (Math.abs(y - (-sqrt(f) * func_scale + FUNC_WIDTH / 2 * func_scale)) <= borderWidth) or
                (Math.abs(y - (sqrt(f) * func_scale + FUNC_WIDTH / 2 * func_scale)) <= borderWidth)
            ) {
                second = true;
            }
            val a = (y >= -sqrt(f) * func_scale + FUNC_WIDTH / 2 * func_scale)
            val b = (y <= sqrt(f) * func_scale + FUNC_WIDTH / 2 * func_scale)

            return ((a and b) to second);
        }
        return false to false
    }

    fun left_function(x: Int, y: Int) = right_function(-x, y)

    fun top_function(x : Int, y: Int) = right_function(y, x)

    fun bottom_function(x : Int, y: Int) = top_function(x, -y)


}