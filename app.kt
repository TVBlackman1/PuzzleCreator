import java.io.File
import java.io.PrintWriter

const val PUZZLE_NUMBER_X = 4 // количество пазлов по x
const val PUZZLE_NUMBER_Y = 4 // количество пазлов по y

const val PART = 0.82 // регулирование размера выемки, корректирует расположение пазла в файле (0 -> 1): Double
const val FILENAME: String = "white.jpg" // название файла
const val DIRECTORY: String = "image/" // дирректория этого файла
const val DIRECTORY_TO: String = "image/puzzle/" // дирректория для хранения паззлов

const val borderWidth = 2 // ширина обводки



fun main(args: Array<String>){
    FolderWork.deleteFromFolder(DIRECTORY_TO)

    println(DIRECTORY + FILENAME)
    val workImage = WorkImage(DIRECTORY + FILENAME)
    val x_div_coordinates = Array(PUZZLE_NUMBER_X+1) {it*workImage.width / PUZZLE_NUMBER_X}
    val y_div_coordinates = Array(PUZZLE_NUMBER_Y+1) {it*workImage.height / PUZZLE_NUMBER_Y}

    val matrix = Array(PUZZLE_NUMBER_Y) {Array<Fragment?>(PUZZLE_NUMBER_X) {null}}
    for(ind_y in 0 until (y_div_coordinates.size - 1)) {
        for(ind_x in 0 until (x_div_coordinates.size - 1)) {

            val a = Point(x_div_coordinates[ind_x], y_div_coordinates[ind_y]) // точка
            val b = Point(x_div_coordinates[ind_x+1], y_div_coordinates[ind_y]) // точка
            val c = Point(x_div_coordinates[ind_x+1], y_div_coordinates[ind_y+1]) // точка
            val d = Point(x_div_coordinates[ind_x], y_div_coordinates[ind_y+1]) // точка
            val rect = Rect(a,b,c,d) // прямоугольник из четырёх точек

            val isRight = (ind_x == PUZZLE_NUMBER_X - 1) // правый элемент изображения
            val isBottom = (ind_y == PUZZLE_NUMBER_Y - 1) // нижний элемент изображения
            val isRotate = ((ind_x+ind_y) % 2 != 0) // на каждом втором фрагменте необходимо делать выемки в других местах
            val leftFragment = if (ind_x > 0) matrix[ind_y][ind_x-1] else null // левый соприкасающийся объект с данным фрагментов
            val topFragment = if (ind_y > 0) matrix[ind_y-1][ind_x] else null // верхний соприкасающийся объект с данным фрагментов
            matrix[ind_y][ind_x] = Fragment(
                rect,
                fr_rotate = isRotate,
                leftFr = leftFragment,
                topFr = topFragment,
                lastRight = isRight,
                lastBottom = isBottom
            )
        }
    }

    val firstImage = matrix[0][0]
    workImage.offset_x = firstImage!!.third_width
    workImage.offset_y = firstImage.third_height
    workImage.widthFragment = (firstImage.b.x - firstImage.a.x) + workImage.offset_x * 2 + 1
    workImage.heightFragment = (firstImage.d.y - firstImage.a.y) + workImage.offset_y * 2 + 1
    var numberFragment = 1
    for (line in matrix){
        for (elem in line){
            println("${numberFragment.toDouble() / PUZZLE_NUMBER_X / PUZZLE_NUMBER_Y * 100}%..")

            workImage.pixelsFor = elem!!.pixels
            workImage.pixelsInner = elem.inner_pixels
            workImage.pixelsBorder = elem.border_pixels
            workImage.firstFragmentX = elem.a.x
            workImage.firstFragmentY = elem.a.y
            workImage.paint()
            workImage.save((numberFragment++).toString())
        }
    }
    println("access")
}
