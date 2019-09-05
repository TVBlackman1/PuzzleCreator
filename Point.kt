data class Point(var x: Int, var y: Int)

/*

    fun bottom() {
        val mode = 1
        if (mode == 1) {
            for (x in (d.x + third_width) .. (d.x + 2 * third_width))
                for (y in (d.y) .. (d.y + third_height))
                    if (top_function(x - d.x - third_width, y - d.y))
                        pixels.add(x to y)
        } else {
            for (x in (d.x + third_width) .. (d.x + 2 * third_width))
                for (y in (d.y - third_height) .. (d.y))
                    if (top_function(x - d.x - third_width, y - d.y))
                        inner_pixels.add(x to y)
        }
        if (mode == 3) {
            for (x in (d.x + third_width) .. (d.x + 2 * third_width))
                for (y in (d.y - third_height) .. (d.y))
                    if (top_function(x - d.x - third_width, y - d.y))
                        inner_pixels.add(x to y)
            for (x in (d.x + third_width) .. (d.x + 2 * third_width))
                for (y in (d.y) .. (d.y + third_height))
                    if (bottom_function(x - d.x - third_width, y - d.y))
                        pixels.add(x to y)
        }
    }

 */