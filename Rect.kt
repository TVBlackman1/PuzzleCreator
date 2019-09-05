import Point

class Rect(val a: Point,
           val b: Point,
           val c: Point,
           val d: Point) {

    operator fun component1() = a
    operator fun component2() = b
    operator fun component3() = c
    operator fun component4() = d
}